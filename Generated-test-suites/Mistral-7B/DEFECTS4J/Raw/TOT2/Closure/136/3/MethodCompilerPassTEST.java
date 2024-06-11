import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

 private static final String TEST_SOURCE = "TestMethodCompilerPass.java";

 /**
 * Test for the default behavior of MethodCompilerPass when there are no externs
 * or methods in the source code.
 */
 @Test
 public void testEmptySource() {
 MethodCompilerPass compilerPass = new MethodCompilerPass(null);
 Node root = null; // empty source code
 compilerPass.process(null, root);
 assertTrue(compilerPass.externMethods.isEmpty());
 assertTrue(compilerPass.externMethodsWithoutSignatures.isEmpty());
 assertTrue(compilerPass.nonMethodProperties.isEmpty());
 assertTrue(compilerPass.methodDefinitions.isEmpty());
 }

 /**
 * Test for the behavior of MethodCompilerPass when there are extern methods
 * defined without signatures.
 */
 @Test
 public void testExternMethodsWithoutSignatures() {
 MethodCompilerPass compilerPass = new MethodCompilerPass(null);
 Node externs = createExternsNode("TestExterns.java");
 Node root = createSourceNode("TestSource.js");
 compilerPass.process(externs, root);

 // Expected results based on the given test source
 Set<String> expectedExternMethods = Sets.newHashSet("foo", "bar");
 Set<String> expectedExternMethodsWithoutSignatures =
 Sets.newHashSet("baz");
 Set<String> expectedNonMethodProperties = Sets.newHashSet();
 Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();

 assertEquals(expectedExternMethods, compilerPass.externMethods);
 assertEquals(expectedExternMethodsWithoutSignatures,
 compilerPass.externMethodsWithoutSignatures);
 assertEquals(expectedNonMethodProperties, compilerPass.nonMethodProperties);
 assertEquals(expectedMethodDefinitions, compilerPass.methodDefinitions);
 }

 /**
 * Test for the behavior of MethodCompilerPass when there are method signatures
 * in the source code.
 */
 @Test
 public void testMethodSignatures() {
 MethodCompilerPass compilerPass = new MethodCompilerPass(null);
 Node externs = null; // empty externs file
 Node root = createSourceNode("TestSource.js");
 compilerPass.process(externs, root);

 // Expected results based on the given test source
 Set<String> expectedExternMethods = Sets.newHashSet();
 Set<String> expectedExternMethodsWithoutSignatures =
 Sets.newHashSet();
 Set<String> expectedNonMethodProperties = Sets.newHashSet("myProperty");
 Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
 expectedMethodDefinitions.put("method1", createNode(
 "function method1() { }"));
 expectedMethodDefinitions.put("method2", createNode(
 "function method2(arg1, arg2) { }"));

 assertEquals(expectedExternMethods, compilerPass.externMethods);
 assertEquals(expectedExternMethodsWithoutSignatures,
 compilerPass.externMethodsWithoutSignatures);
 assertEquals(expectedNonMethodProperties, compilerPass.nonMethodProperties);
 assertEquals(expectedMethodDefinitions, compilerPass.methodDefinitions);
 }

 /**
 * Test for the behavior of MethodCompilerPass when there are method signatures
 * in the source code and an extern method without a signature.
 */
 @Test
 public void testMethodSignaturesWithExternMethodWithoutSignature() {
 MethodCompilerPass compilerPass = new MethodCompilerPass(null);
 Node externs = createExternsNode("TestExterns.java");
 Node root = createSourceNode("TestSource.js");
 compilerPass.process(externs, root);

 // Expected results based on the given test source
 Set<String> expectedExternMethods = Sets.newHashSet("foo");
 Set<String> expectedExternMethodsWithoutSignatures =
 Sets.newHashSet("bar");
 Set<String> expectedNonMethodProperties = Sets.newHashSet("myProperty");
 Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
 expectedMethodDefinitions.put("method1", createNode(
 "function method1() { }"));
 expectedMethodDefinitions.put("method2", createNode(
 "function method2(arg1, arg2) { }"));

 assertEquals(expectedExternMethods, compilerPass.externMethods);
 assertEquals(expectedExternMethodsWithoutSignatures,
 compilerPass.externMethodsWithoutSignatures);
 assertEquals(expectedNonMethodProperties, compilerPass.nonMethodProperties);
 assertEquals(expectedMethodDefinitions, compilerPass.methodDefinitions);
 }

 /**
 * Helper method to create a Node representing an empty externs file.
 */
 private Node createExternsNode(String fileName) {
 return createNode("// empty file: " + fileName);
 }

 /**
 * Helper method to create a Node representing a source file with no methods or
 * externs.
 */
 private Node createSourceNode(String fileName) {
 return createNode("// empty file: " + fileName);
 }

 /**
 * Helper method to create a Node representing a source file with methods.
 */
 private Node createSourceNodeWithMethods(String method1, String method2) {
 return createNode("" +
 "function method1() { }" +
 "function method2(arg1, arg2) { }" +
 "function main() { }");
 }

 /**
 * Helper method to create a Node representing a source file with methods and an
 * extern method without a signature.
 */
 private Node createSourceNodeWithExternAndMethods(String method1,
 String method2, String externMethod) {
 return createNode("" +
 "function method1() { }" +
 "function method2(arg1, arg2) { }" +
 "function " + externMethod + "() { }" +
 "function main() { }");
 }

 /**
 * Helper method to create a Node.
 */
 private Node createNode(String nodeContent) {
 Node node = new Node();
 node.setType(Token.PROGRAM);
 node.setSourceName(TEST_SOURCE);
 Node children = node.getFirstChild();
 children = createNodesFromString(nodeContent, children);
 node.setFirstChild(children);
 return node;
 }

 /**
 * Helper method to create Node children from a string.
 */
 private Node createNodesFromString(String content, Node parent) {
 Node children = parent;
 String[] lines = content.split("\\r?\\n");
 for (String line : lines) {
 Node lineNode = createNodeFromString(line);
 children = children.addChild(lineNode);
 }
 return children;
 }

 /**
 * Helper method to create a Node from a string.
 */
 private Node createNodeFromString(String nodeContent) {
 Node node = new Node();
 String[] parts = nodeContent.split("\\s+");
 NodeType nodeType = NodeType.fromToken(parts[0].charAt(0));
 node.setType(nodeType.getToken());
 node.setSourceName(TEST_SOURCE);
 node.setString(nodeContent);
 return node;
 }

 /**
 * Helper enum for Node types.
 */
 private enum NodeType {
 INSTANCE;

 NodeType(Token token) {
 this();
 }

 NodeType(char token) {
 this();
 setToken(token);
 }

 NodeType setToken(Token token) {
 this.token = token;
 return this;
 }

 Token getToken() {
 return token;
 }

 private Token token;
 }
}