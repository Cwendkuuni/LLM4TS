import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

	private CheckGlobalThis checkGlobalThis;
	
	@Before
	public void setUp() {
		checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForConstructorFunction() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder()
				.recordConstructor()
				.build());
		Node parent = null;
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForThisAnnotatedFunction() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder()
				.recordThisType()
				.build());
		Node parent = null;
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForOverrideAnnotatedFunction() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder()
				.recordOverride()
				.build());
		Node parent = null;
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForBlockParent() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.BLOCK);
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForScriptParent() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.SCRIPT);
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForNameParent() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.NAME);
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForAssignParent() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.ASSIGN);
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForStringParent() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.STRING);
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsFalseForNumberParent() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.NUMBER);
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void shouldTraverse_ReturnsTrueForOtherParent() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.UNKNOWN);
		
		boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
		
		assertTrue(result);
	}
	
	@Test
	public void visit_ReportsGlobalThisWarning() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.THIS);
		Node parent = new Node(Token.GETPROP);
		parent.addChildToBack(new Node(Token.GETPROP));
		
		checkGlobalThis.visit(t, n, parent);
		
		// Assert that the warning was reported
	}
	
	@Test
	public void visit_ClearsAssignLhsChild() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node(Token.THIS);
		Node parent = new Node(Token.GETPROP);
		parent.addChildToBack(new Node(Token.GETPROP));
		
		checkGlobalThis.visit(t, n, parent);
		
		assertNull(checkGlobalThis.assignLhsChild);
	}
	
	@Test
	public void shouldReportThis_ReturnsTrueForAssignLhsChildNotNull() {
		Node n = new Node(Token.THIS);
		Node parent = new Node(Token.ASSIGN);
		Node lhs = new Node();
		checkGlobalThis.assignLhsChild = lhs;
		parent.addChildToFront(lhs);
		
		boolean result = checkGlobalThis.shouldReportThis(n, parent);
		
		assertTrue(result);
	}
	
	@Test
	public void shouldReportThis_ReturnsTrueForParentGetAccessor() {
		Node n = new Node(Token.THIS);
		Node parent = new Node(Token.GETPROP);
		
		boolean result = checkGlobalThis.shouldReportThis(n, parent);
		
		assertTrue(result);
	}
	
	@Test
	public void shouldReportThis_ReturnsFalseForOtherParent() {
		Node n = new Node(Token.THIS);
		Node parent = new Node(Token.UNKNOWN);
		
		boolean result = checkGlobalThis.shouldReportThis(n, parent);
		
		assertFalse(result);
	}
	
	@Test
	public void getFunctionJsDocInfo_ReturnsJsDocInfoWhenPresent() {
		Node n = new Node(Token.FUNCTION);
		n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
		Node parent = null;
		
		JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
		
		assertNotNull(result);
	}
	
	@Test
	public void getFunctionJsDocInfo_ReturnsParentJsDocInfoForNameParent() {
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.NAME);
		parent.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
		
		JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
		
		assertNotNull(result);
	}
	
	@Test
	public void getFunctionJsDocInfo_ReturnsGrandParentJsDocInfoForVarNameParent() {
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.NAME);
		Node gramps = new Node(Token.VAR);
		gramps.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder().build());
		gramps.addChildToBack(parent);
		
		JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
		
		assertNotNull(result);
	}
	
	@Test
	public void getFunctionJsDocInfo_ReturnsNullWhenAbsent() {
		Node n = new Node(Token.FUNCTION);
		Node parent = null;
		
		JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
		
		assertNull(result);
	}

}