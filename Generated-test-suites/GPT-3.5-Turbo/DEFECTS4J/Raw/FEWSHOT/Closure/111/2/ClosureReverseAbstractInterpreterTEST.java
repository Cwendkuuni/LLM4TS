import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        CodingConvention convention = new CodingConvention();
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
        
        Node condition = new Node();
        FlowScope blindScope = new FlowScope();
        boolean outcome = false;
        
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        
        // TODO: Write assertion statements and complete the test case
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
        
        Node parameter = new Node();
        JSType type = new JSType();
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                return null;
            }
        };
        boolean outcome = true;
        
        FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        
        // TODO: Write assertion statements and complete the test case
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("\"", Util.stripLeadingAndTrailingQuotes("\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}