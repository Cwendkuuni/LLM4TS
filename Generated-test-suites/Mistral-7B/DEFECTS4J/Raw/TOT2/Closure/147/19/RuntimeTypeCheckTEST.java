import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.assertEquals;

public class RuntimeTypeCheckTest {

 private Compiler compiler;
 private AbstractCompiler abstractCompiler;
 private String logFunction;

 @BeforeClass
 public static void setupClass() {
 // Initialize the compiler and the logging function (if provided)
 }

 @Before
 public void setup() {
 // Initialize the test environment for each test case
 compiler = new Compiler();
 abstractCompiler = new RuntimeTypeCheck(compiler, logFunction);
 }

 @After
 public void tearDown() {
 // Clean up the test environment after each test case
 compiler = null;
 abstractCompiler = null;
 }

 @Test
 public void testAddMarkers() {
 String testCode = "function Foo() {}";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node expected = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
 Node actual = findMarkerNode(node, "Foo");
 assertEquals(expected, actual);
 }

 @Test
 public void testAddMarkersForInterface() {
 String testCode = "function Foo() {} function FooBar() implements Foo {}";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node expectedInterfaceMarker = NodeUtil.newQualifiedNameNode("FooBar", -1, -1);
 Node expectedClassMarker = NodeUtil.newQualifiedNameNode("FooBar", -1, -1);
 Node interfaceMarker = findMarkerNode(node, "FooBar");
 Node classMarker = findMarkerNode(node, "FooBar");
 assertEquals(expectedInterfaceMarker, interfaceMarker);
 assertEquals(expectedClassMarker, classMarker);
 }

 @Test
 public void testAddChecksForFunction() {
 String testCode = "function Foo(x: string, y: number) { return x + y; }";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode1 = findCheckNode(node, "string");
 Node checkNode2 = findCheckNode(node, "number");
 expectCheckNodes(node, checkNode1, checkNode2);
 }

 @Test
 public void testAddChecksForFunctionWithVoidReturnType() {
 String testCode = "function Foo() { }";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode = findCheckNode(node, "void");
 expectCheckNodes(node, checkNode, null);
 }

 @Test
 public void testAddChecksForFunctionWithMultipleReturnTypes() {
 String testCode = "function Foo(x: number) { return x > 0 ? x : 'foo'; }";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode1 = findCheckNode(node, "number");
 Node checkNode2 = findCheckNode(node, "string");
 expectCheckNodes(node, checkNode1, checkNode2);
 }

 @Test
 public void testAddChecksForFunctionWithFunctionParameter() {
 String testCode = "function Foo(x: Function) { x(); }";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode = findCheckNode(node, "Function");
 expectCheckNodes(node, checkNode, null);
 }

 @Test
 public void testAddChecksForFunctionWithInterfaceParameter() {
 String testCode = "function Foo(x: Bar) { } function Bar() {}";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode = findCheckNode(node, "Bar");
 expectCheckNodes(node, checkNode, null);
 }

 @Test
 public void testAddChecksForFunctionWithExternedInterfaceParameter() {
 String testCode = "function Foo(x: Bar) { } /** @type {!Bar} */ var Bar = {};";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode = findCheckNode(node, "Bar");
 expectCheckNodes(node, checkNode, null);
 }

 @Test
 public void testAddChecksForFunctionWithExternedClassParameter() {
 String testCode = "function Foo(x: Foo) { } /** @type {!Foo} */ var Foo = {};";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode = findCheckNode(node, "Foo");
 expectCheckNodes(node, checkNode, null);
 }

 @Test
 public void testAddChecksForFunctionWithClassParameter() {
 String testCode = "function Foo(x) { } class Foo {}";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode = findCheckNode(node, "Foo");
 expectCheckNodes(node, checkNode, null);
 }

 @Test
 public void testAddChecksForFunctionWithNamedInterfaceParameter() {
 String testCode = "function Foo(x: MyInterface) { } interface MyInterface {}";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode = findCheckNode(node, "MyInterface");
 expectCheckNodes(node, checkNode, null);
 }

 @Test
 public void testAddChecksForFunctionWithMultipleParameterTypes() {
 String testCode = "function Foo(x: number, y: string) { return x + y; }";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode1 = findCheckNode(node, "number");
 Node checkNode2 = findCheckNode(node, "string");
 expectCheckNodes(node, checkNode1, checkNode2);
 }

 @Test
 public void testAddChecksForFunctionWithComplexTypes() {
 String testCode = "function Foo(x: (string|number)[], y: (number|Function)) { return x[0] + y(); }";
 Node node = NodeUtil.parseJs(testCode);
 abstractCompiler.process(null, node);
 Node checkNode1 = findCheckNode(node, "string");
 Node checkNode2 = findCheckNode(node, "number");
 Node checkNode3 = findCheckNode(node, "Function");
 expectCheckNodes(node, checkNode1, checkNode2, checkNode3);
 }

 private Node findMarkerNode(Node node, String className) {
 Node marker = null;
 NodeTraversal.traverse(compiler, node, new NodeTraversal.AbstractPostOrderCallback() {
 @Override public void visit(NodeTraversal t, Node n, Node parent) {
 if (n.getType() == Token.GETPROP && n.getFirstChild().getText().equals("prototype")) {
 ObjectType objectType = n.getParent().getJSType();
 for (ObjectType interfaceType : objectType.getAllImplementedInterfaces()) {
 if (interfaceType.getReferenceName().equals(className)) {
 marker = n;
 return;
 }
 }
 if (objectType.getReferenceName().equals(className)) {
 marker = n;
 return;
 }
 }
 }
 });
 return marker;
 }

 private Node findCheckNode(Node node, String typeString) {
 Node checkNode = null;
 NodeTraversal.traverse(compiler, node, new NodeTraversal.AbstractPostOrderCallback() {
 @Override public void visit(NodeTraversal t, Node n, Node parent) {
 if (n.getType() == Token.CALL && n.getFirstChild().getText().equals("checkType")) {
 Node arrayNode = n.getLastChild();
 for (int i = 0; i < arrayNode.getNumChildren(); i++) {
 Node checkerNode = arrayNode.getChild(i);
 if (checkerNode.getFirstChild().getText().equals(typeString)) {
 checkNode = checkerNode;
 return;
 }
 }
 }
 }
 });
 return checkNode;
 }

 private void expectCheckNodes(Node node, Node... checkNodes) {
 int numCheckNodes = checkNodes.length;
 Node checkNode = null;
 for (int i = 0; i < numCheckNodes; i++) {
 checkNode = findCheckNode(node, checkNodes[i].toString());
 assertEquals(checkNodes[i], checkNode);
 }
 }
}