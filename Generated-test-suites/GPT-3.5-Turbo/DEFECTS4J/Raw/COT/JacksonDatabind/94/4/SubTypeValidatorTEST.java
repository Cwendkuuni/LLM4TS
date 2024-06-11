import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = new JavaType();

        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = new JavaType("com.fasterxml.jackson.annotation.JsonIgnore");

        subTypeValidator.validateSubType(ctxt, type);
    }
}