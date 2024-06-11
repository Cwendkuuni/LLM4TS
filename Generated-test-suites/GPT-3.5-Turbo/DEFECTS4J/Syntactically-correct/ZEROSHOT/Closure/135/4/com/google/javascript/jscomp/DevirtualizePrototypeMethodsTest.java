import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {

    private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
    }

    @Test
    public void testIsCall() {
        UseSite site = new UseSite();
        site.node = new Node(Token.CALL, new Node(Token.NAME, "foo"));
        assertTrue(devirtualizePrototypeMethods.isCall(site));
    }

    @Test
    public void testIsNotCall() {
        UseSite site = new UseSite();
        site.node = new Node(Token.NAME, "foo");
        assertFalse(devirtualizePrototypeMethods.isCall(site));
    }

    @Test
    public void testIsPrototypeMethodDefinition() {
        Node node = new Node(Token.NAME, "A.prototype.accumulate");
        assertTrue(devirtualizePrototypeMethods.isPrototypeMethodDefinition(node));
    }

    @Test
    public void testIsNotPrototypeMethodDefinition() {
        Node node = new Node(Token.NAME, "A.prototype.foo");
        assertFalse(devirtualizePrototypeMethods.isPrototypeMethodDefinition(node));
    }

    @Test
    public void testGetRewrittenMethodName() {
        String originalMethodName = "accumulate";
        String expectedMethodName = "JSCompiler_StaticMethods_accumulate";
        assertEquals(expectedMethodName, devirtualizePrototypeMethods.getRewrittenMethodName(originalMethodName));
    }

    // Add more test methods here...

}