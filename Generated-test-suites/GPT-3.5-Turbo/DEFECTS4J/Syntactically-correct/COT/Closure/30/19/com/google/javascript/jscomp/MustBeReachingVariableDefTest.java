import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

	@Test
	public void testGetDef() {
		MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef();
		Node node = new Node(Token.VAR);
		node.addChildToFront(new Node(Token.NAME, new Node(Token.NAME)));
		Node useNode = new Node(Token.NAME, new Node(Token.NAME));
		Node def = analyzer.getDef("name", useNode);
		assertEquals(node, def);
	}

	@Test
	public void testDependsOnOuterScopeVars() {
		MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef();
		Node node = new Node(Token.VAR);
		node.addChildToFront(new Node(Token.NAME, new Node(Token.NAME)));
		Node useNode = new Node(Token.NAME, new Node(Token.NAME));
		boolean result = analyzer.dependsOnOuterScopeVars("name", useNode);
		assertFalse(result);
	}

}