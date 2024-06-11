import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;
import static org.junit.Assert.assertEquals;

public class RuntimeTypeCheckTest {

    private Node externs;
    private Node root;
    private CompilerPass pass;
    private AbstractCompiler compiler;

    @Before
    public void setup() {
        externs = new Node(Token.SCRIPT);
        root = new Node(Token.SCRIPT);
        compiler = new AbstractCompiler();
        pass = new RuntimeTypeCheck(compiler, null);
    }

    @Test
    public void testProcess_addMarkers() {
        Node function = createConstructorFunction();
        root.addChildToFront(function);
        pass.process(externs, root);
        Node expectedNode = createMarkerNode();
        assertNodesEqual(expectedNode, root.getFirstChild().getFirstChild().getLastChild());
    }

    @Test
    public void testProcess_addChecksForParameters() {
        Node function = createFunctionWithParameters();
        root.addChildToFront(function);
        pass.process(externs, root);
        Node expectedNode = createCheckTypeCallNode("string", "name");
        Node actualNode = root.getFirstChild().getLastChild().getLastChild().getLastChild();
        assertNodesEqual(expectedNode, actualNode);
    }

    @Test
    public void testProcess_addCheckForReturnType() {
        Node function = createFunctionWithReturnType();
        root.addChildToFront(function);
        pass.process(externs, root);
        Node expectedNode = createCheckTypeCallNode("boolean", "result");
        Node actualNode = root.getFirstChild().getLastChild().getLastChild().getLastChild();
        assertNodesEqual(expectedNode, actualNode);
    }

    private Node createConstructorFunction() {
        Node function = new Node(Token.FUNCTION);
        function.putBooleanProp(Node.FUNCTION_PROPERTY, true);
        function.addChildToBack(createnameNode("constructor"));
        return function;
    }

    private Node createnameNode(String name) {
        Node nameNode = Node.newString(name);
        nameNode.setLineno(1);
        nameNode.setCharno(1);
        return nameNode;
    }

    private Node createMarkerNode() {
        Node markerNode = Node.newString("instance_of__constructor");
        Node assignNode = new Node(Token.EXPR_RESULT);
        Node getElemNode = new Node(Token.GETELEM);
        Node getPropNode = new Node(Token.GETPROP);
        Node classNode = Node.newQualifiedNameNode("constructor", -1, -1);
        Node markerVarNode = new Node(Token.STRING, markerNode);
        getPropNode.addChildToBack(classNode);
        getPropNode.addChildToBack(markerVarNode);
        getElemNode.addChildToBack(getPropNode);
        getElemNode.addChildToBack(Node.newBoolean(true));
        assignNode.addChildToBack(getElemNode);
        return assignNode;
    }

    private Node createFunctionWithParameters() {
        Node function = new Node(Token.FUNCTION);
        function.putBooleanProp(Node.FUNCTION_PROPERTY, true);
        function.addChildToBack(createnameNode("testFunction"));
        function.addChildToBack(createnameNode("name"));
        return function;
    }

    private Node createCheckTypeCallNode(String type, String parameterName) {
        Node callNode = new Node(Token.CALL);
        callNode.addChildToBack(jsCode("checkType"));
        callNode.addChildToBack(createnameNode(parameterName));
        Node arrayNode = new Node(Token.ARRAYLIT);
        Node checkerNode = new Node(Token.CALL);
        checkerNode.addChildToBack(jsCode("valueChecker"));
        checkerNode.addChildToBack(Node.newString(type));
        arrayNode.addChildToBack(checkerNode);
        callNode.addChildToBack(arrayNode);
        return callNode;
    }

    private Node jsCode(String prop) {
        return NodeUtil.newQualifiedNameNode("jscomp.typecheck." + prop, -1, -1);
    }

    private Node createFunctionWithReturnType() {
        Node function = new Node(Token.FUNCTION);
        function.putBooleanProp(Node.FUNCTION_PROPERTY, true);
        function.addChildToBack(createnameNode("testFunction"));
        function.addChildToBack(new Node(Token.VAR, createnameNode("result")));
        return function;
    }

    private void assertNodesEqual(Node expected, Node actual) {
        assertEquals(expected.getToken(), actual.getToken());
        assertEquals(expected.getBooleanProp(Node.FUNCTION_PROPERTY), actual.getBooleanProp(Node.FUNCTION_PROPERTY));
        assertEquals(expected.isString(), actual.isString());
        assertEquals(expected.getString(), actual.getString());
        assertEquals(expected.getLineno(), actual.getLineno());
        assertEquals(expected.getCharno(), actual.getCharno());
        assertEquals(expected.hasChildren(), actual.hasChildren());
        assertEquals(expected.getChildCount(), actual.getChildCount());
    }
}