import static org.junit.Assert.*;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

    private ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);

    @Test
    public void testIsDef() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isDef").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isDef").apply(restriction));
    }
    
    @Test
    public void testIsNull() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isNull").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isNull").apply(restriction));
    }
    
    @Test
    public void testIsDefAndNotNull() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isDefAndNotNull").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isDefAndNotNull").apply(restriction));
    }
    
    @Test
    public void testIsString() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isString").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isString").apply(restriction));
    }
    
    @Test
    public void testIsBoolean() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isBoolean").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isBoolean").apply(restriction));
    }
    
    @Test
    public void testIsNumber() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isNumber").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isNumber").apply(restriction));
    }
    
    @Test
    public void testIsFunction() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isFunction").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isFunction").apply(restriction));
    }
    
    @Test
    public void testIsArray() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isArray").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isArray").apply(restriction));
    }
    
    @Test
    public void testIsObject() {
        TypeRestriction restriction = new TypeRestriction(null, true);
        assertNotNull(interpreter.restricters.get("isObject").apply(restriction));
        
        restriction = new TypeRestriction(null, false);
        assertNull(interpreter.restricters.get("isObject").apply(restriction));
    }
}