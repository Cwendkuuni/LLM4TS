import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertThrows;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeTypes;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubTypeNullType() {
 Assert.assertNull(validator.validateSubType(null, null));
 }

 @Test
 public void testValidateSubTypeNullContext() {
 DeserializationContext context = null;
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
 }

 @Test
 public void testValidateSubTypeValidType() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test
 public void testValidateSubTypeNastyClass() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(org.apache.commons.collections.functors.InstantiateTransformer.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), type));
 }

 @Test
 public void testValidateSubTypeSpringClass() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), type));
 }

 @Test
 public void testValidateSubTypeSpringInterface() {
 JavaType type = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.BeanFactory.class);
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test
 public void testValidateSubTypeEmptyString() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(new DeserializationContext(null), TypeTypes.STRING_TYPE);
 }

 @Test
 public void testValidateSubTypeEmptyClassName() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(new StringType(""));
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test(expected = JsonProcessingException.class)
 public void testValidateSubTypeInvalidType() throws JsonMappingException {
 JavaType type = TypeFactory.defaultInstance().constructType(int[].class);
 validator.validateSubType(new DeserializationContext(null), type);
 }

 @Test
 public void testValidateSubTypeSubClassOfNastyClass() throws JsonMappingException {
 JavaType superType = TypeFactory.defaultInstance().constructType(org.apache.commons.collections.functors.InstantiateTransformer.class);
 JavaType subType = TypeFactory.defaultInstance().constructType(org.apache.commons.collections.Transformer.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), subType.getType(superType)));
 }

 @Test
 public void testValidateSubTypeSubInterfaceOfSpringClass() throws JsonMappingException {
 JavaType superType = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.BeanDefinition.class);
 JavaType subType = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.PropertyDefinition.class);
 validator.validateSubType(new DeserializationContext(null), subType.getType(superType));
 }

 @Test
 public void testValidateSubTypeSubInterfaceOfSpringInterface() throws JsonMappingException {
 JavaType superType = TypeFactory.defaultInstance().constructType(javax.xml.XMLConstants.class);
 JavaType subType = TypeFactory.defaultInstance().constructType(java.io.Serializable.class);
 validator.validateSubType(new DeserializationContext(null), subType.getType(superType));
 }
}