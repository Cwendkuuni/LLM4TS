import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testValidateSubType_ValidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory().constructType(SubTypeValidator.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springClass = objectMapper.getTypeFactory().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), springClass);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NastyClass() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectMapper testMapper = new ObjectMapper(objectMapper);
 testMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 testMapper.setVisibilityChecker(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
 .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
 testMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyClass = testMapper.getTypeFactory().constructType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);
 try {
 validator.validateSubType(testMapper.getDeserializationContext(), nastyClass);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}