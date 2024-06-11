import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    @Test
    public void testValidSubType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        SubTypeValidator validator = mapper.readerForUpdating(null).readValue(mapper.writeValueAsString(new SubTypeValidator()));
        assertNotNull(validator);
        validator.validateSubType(null, validType);
    }

    @Test
    public void testInstance() {
        assertSame(SubTypeValidator.instance(), SubTypeValidator.instance());
    }

    @Test(expected = JsonMappingException.class)
    public void testIllegalClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType illegalType = TypeFactory.defaultInstance().constructType(new Object() {}.getClass());
        SubTypeValidator validator = mapper.readerForUpdating(null).readValue(mapper.writeValueAsString(new SubTypeValidator()));
        validator.validateSubType(null, illegalType);
    }

    @Test(expected = JsonMappingException.class)
    public void testBlockedClassStartingWithPrefix() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType blockedClass = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
        SubTypeValidator validator = mapper.readerForUpdating(null).readValue(mapper.writeValueAsString(new SubTypeValidator()));
        validator.validateSubType(null, blockedClass);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullArguments() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        SubTypeValidator validator = mapper.readerForUpdating(null).readValue(mapper.writeValueAsString(new SubTypeValidator()));
        validator.validateSubType(null, validType);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullReturnValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        SubTypeValidator validator = mapper.readerForUpdating(null).readValue(mapper.writeValueAsString(new SubTypeValidator()));
        assertNull(validator.validateSubType(null, validType));
    }

    @Test
    public void testValidateSubTypeWithValidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
        SubTypeValidator validator = mapper.readerForUpdating(null).readValue(mapper.writeValueAsString(new SubTypeValidator()));
        assertNotNull(validator.validateSubType(null, validType));
    }

    @Test
    public void testValidateSubTypeWithInvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType invalidType = TypeFactory.defaultInstance().constructType(Object.class);
        SubTypeValidator validator = mapper.readerForUpdating(null).readValue(mapper.writeValueAsString(new SubTypeValidator()));
        assertNull(validator.validateSubType(null, invalidType));
    }

}