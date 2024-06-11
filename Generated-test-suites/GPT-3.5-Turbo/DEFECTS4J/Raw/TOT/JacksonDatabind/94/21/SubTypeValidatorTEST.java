import static org.junit.Assert.*;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        ctxt = new DeserializationContext(null, null);
        type = JavaType.nullType();
    }

    @Test
    public void testValidateSubType_ValidType_NoExceptionThrown() {
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Should not throw any exceptions for a valid type.");
        }
    }

    @Test
    public void testValidateSubType_NastyClass_ExceptionThrown() {
        type = JavaType.fromClass(org.apache.commons.collections.functors.InvokerTransformer.class);
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("JsonMappingException should be thrown for a nasty class type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons",
                    e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_SpringFrameworkClass_ExceptionThrown() {
        type = JavaType.fromClass(org.springframework.beans.factory.ObjectFactory.class);
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("JsonMappingException should be thrown for a Spring Framework class type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons",
                    e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_NoDeserClass_ExceptionThrown() {
        type = JavaType.fromClass(com.sun.rowset.JdbcRowSetImpl.class);
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("JsonMappingException should be thrown for a no-deser class type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.sun.rowset.JdbcRowSetImpl) to deserialize: prevented for security reasons",
                    e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_UnknownClass_ExceptionThrown() {
        type = JavaType.fromClass(UnknownClass.class);
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("JsonMappingException should be thrown for an unknown class type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (UnknownClass) to deserialize: prevented for security reasons",
                    e.getMessage());
        }
    }
}