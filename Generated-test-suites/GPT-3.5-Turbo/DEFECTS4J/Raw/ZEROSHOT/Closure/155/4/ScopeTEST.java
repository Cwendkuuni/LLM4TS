import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.jscomp.Scope;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

    private Scope scope;

    @Before
    public void setUp() {
        scope = new Scope(null, null);
    }

    @Test
    public void testDeclare() {
        Var var = scope.declare("test", new Node(Token.NAME), null, null);
        assertEquals("test", var.getName());
    }

    @Test
    public void testUndeclare() {
        Var var = scope.declare("test", new Node(Token.NAME), null, null);
        scope.undeclare(var);
        assertEquals(null, scope.getVar("test"));
    }

    @Test
    public void testGetVar() {
        Var var = scope.declare("test", new Node(Token.NAME), null, null);
        assertEquals(var, scope.getVar("test"));
        assertEquals(null, scope.getVar("nonexistent"));
    }

    @Test
    public void testIsDeclared() {
        scope.declare("test", new Node(Token.NAME), null, null);
        assertEquals(true, scope.isDeclared("test", true));
        assertEquals(false, scope.isDeclared("nonexistent", true));
    }

    @Test
    public void testIsGlobal() {
        assertEquals(true, scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        assertEquals(false, scope.isLocal());
    }
}