import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

  private SubTypeValidator subTypeValidator;
  private DeserializationContext deserializationContext;
  private JavaType javaType;

  @Before
  public void setup() {
    subTypeValidator = SubTypeValidator.instance();
    deserializationContext = Mockito.mock(DeserializationContext.class);
    javaType = Mockito.mock(JavaType.class);
    Mockito.when(javaType.getRawClass()).thenReturn(Object.class);
  }

  @Test
  public void testValidateSubType_ValidType_NoExceptionThrown() {
    try {
      subTypeValidator.validateSubType(deserializationContext, javaType);
    } catch (JsonMappingException e) {
      fail("No exception should be thrown for a valid type");
    }
  }

  @Test
  public void testValidateSubType_IllegalType_ExceptionThrown() {
    try {
      Mockito.when(javaType.getRawClass()).thenReturn(AbnormalClass.class);
      subTypeValidator.validateSubType(deserializationContext, javaType);
      fail("Exception should be thrown for an illegal type");
    } catch (JsonMappingException e) {
      // Exception should be thrown as the type is illegal
    }
  }

  @Test
  public void testValidateSubType_SpringFrameworkClass_ThrowsException() {
    try {
      Mockito.when(javaType.getRawClass()).thenReturn(AbstractApplicationContext.class);
      subTypeValidator.validateSubType(deserializationContext, javaType);
      fail("Exception should be thrown for a Spring framework class");
    } catch (JsonMappingException e) {
      // Exception should be thrown as the Spring framework class is illegal
    }
  }

  @Test
  public void testValidateSubType_AbstractPointcutAdvisor_ThrowsException() {
    try {
      Mockito.when(javaType.getRawClass()).thenReturn(AbstractPointcutAdvisor.class);
      subTypeValidator.validateSubType(deserializationContext, javaType);
      fail("Exception should be thrown for AbstractPointcutAdvisor class");
    } catch (JsonMappingException e) {
      // Exception should be thrown as the AbstractPointcutAdvisor class is illegal
    }
  }

}