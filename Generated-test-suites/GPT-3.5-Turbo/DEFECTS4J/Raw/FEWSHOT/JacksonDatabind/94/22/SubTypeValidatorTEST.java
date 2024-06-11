import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        subTypeValidator.validateSubType(ctxt, type);
    }
}