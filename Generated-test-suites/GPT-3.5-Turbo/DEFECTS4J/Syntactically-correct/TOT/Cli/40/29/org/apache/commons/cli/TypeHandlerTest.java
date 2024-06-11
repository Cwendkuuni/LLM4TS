import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }
    
    @Test
    public void testCreateValue_stringValue() {
        try {
            String str = "test";
            Object result = typeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
            assertEquals(str, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }
    
    @Test
    public void testCreateValue_objectValue() {
        try {
            String str = "java.lang.Integer";
            Object result = typeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
            assertEquals(Integer.class, result.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }
    
    @Test
    public void testCreateValue_numberValue() {
        try {
            String str = "10";
            Object result = typeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
            assertEquals(Long.class, result.getClass());
            assertEquals(10L, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }
    
    // Add more test cases...

}