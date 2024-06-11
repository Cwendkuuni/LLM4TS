import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_withValidType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = JavaType.defaultInstance();

        try {
            validator.validateSubType(context, type);
        } catch (JsonMappingException e) {
            fail("Exception thrown for valid type");
        }
    }

    @Test(expected=JsonMappingException.class)
    public void testValidateSubType_withIllegalType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = JavaType.defaultInstance();

        // Setting illegal class name to trigger exception
        Set<String> illegalClassNames = new HashSet<String>(SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES);
        illegalClassNames.add(type.getRawClass().getName());
        validator.setIllegalClassNames(illegalClassNames);

        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_withSpringClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, null);
        JavaType type = JavaType.defaultInstance();

        // Setting spring class name to trigger exception
        Set<String> illegalClassNames = new HashSet<String>();
        illegalClassNames.add("org.springframework.beans.factory.ObjectFactory");
        validator.setIllegalClassNames(illegalClassNames);

        try {
            validator.validateSubType(context, type);
        } catch (JsonMappingException e) {
            fail("Exception thrown for valid spring class");
        }
    }

}