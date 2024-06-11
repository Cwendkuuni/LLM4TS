import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.type.TypeReference;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException, JsonProcessingException {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(mock(DeserializationContext.class), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalType_SimpleName() throws JsonMappingException, JsonProcessingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(Object.class, "org.apache.commons.collections.functors.InvokerTransformer");
 validator.validateSubType(mock(DeserializationContext.class), illegalType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalType_FullName() throws JsonMappingException, JsonProcessingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(Object.class, "org.apache.commons.collections.functors.InvokerTransformer.class");
 validator.validateSubType(mock(DeserializationContext.class), illegalType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalType_StartsWithPrefix() throws JsonMappingException, JsonProcessingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(Object.class, "org.springframework.beans.factory.ObjectFactory");
 validator.validateSubType(mock(DeserializationContext.class), illegalType);
 }

 @Test
 public void testValidateSubType_IllegalType_NotInList() throws JsonMappingException, JsonProcessingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(Object.class, "com.test.MyCustomClass");
 validator.validateSubType(mock(DeserializationContext.class), illegalType);
 }
}