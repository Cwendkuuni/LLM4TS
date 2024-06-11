import static org.junit.Assert.*;

import org.junit.Test;

public class PropertyBuilderTest {

   @Test
   public void testBuildWriter() {
      // Create test objects
      SerializationConfig config = new SerializationConfig();
      BeanDescription beanDesc = new BeanDescription();
      PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
      
      // Test case 1: Inclusion mode is NON_DEFAULT
      {
         BeanPropertyDefinition propDef = new BeanPropertyDefinition();
         JavaType declaredType = new JavaType();
         JsonSerializer<?> ser = new JsonSerializer<>();
         TypeSerializer typeSer = new TypeSerializer();
         TypeSerializer contentTypeSer = new TypeSerializer();
         AnnotatedMember am = new AnnotatedMember();
         boolean defaultUseStaticTyping = false;
         
         BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
         
         // Assertion statements
         // ...
      }
      
      // Test case 2: Inclusion mode is NON_ABSENT
      {
         BeanPropertyDefinition propDef = new BeanPropertyDefinition();
         JavaType declaredType = new JavaType();
         JsonSerializer<?> ser = new JsonSerializer<>();
         TypeSerializer typeSer = new TypeSerializer();
         TypeSerializer contentTypeSer = new TypeSerializer();
         AnnotatedMember am = new AnnotatedMember();
         boolean defaultUseStaticTyping = false;
         
         BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
         
         // Assertion statements
         // ...
      }
      
      // Test case 3: Inclusion mode is NON_EMPTY
      {
         BeanPropertyDefinition propDef = new BeanPropertyDefinition();
         JavaType declaredType = new JavaType();
         JsonSerializer<?> ser = new JsonSerializer<>();
         TypeSerializer typeSer = new TypeSerializer();
         TypeSerializer contentTypeSer = new TypeSerializer();
         AnnotatedMember am = new AnnotatedMember();
         boolean defaultUseStaticTyping = false;
         
         BeanPropertyWriter writer = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
         
         // Assertion statements
         // ...
      }
   }
}