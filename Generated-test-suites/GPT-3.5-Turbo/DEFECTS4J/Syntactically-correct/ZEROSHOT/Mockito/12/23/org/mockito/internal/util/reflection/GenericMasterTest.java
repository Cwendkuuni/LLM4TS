import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

  @Test
  public void testGetGenericType_NonGenericField() {
    // Create a new instance of GenericMaster
    GenericMaster genericMaster = new GenericMaster();
    
    // Create a mock Field
    Field field = mock(Field.class);

    // Set up the mock Field to return null for getGenericType()
    when(field.getGenericType()).thenReturn(null);
    
    // Call the getGenericType() method and assert the result
    Class result = genericMaster.getGenericType(field);
    assertEquals(Object.class, result);
  }
  
  @Test
  public void testGetGenericType_GenericField() {
    // Create a new instance of GenericMaster
    GenericMaster genericMaster = new GenericMaster();
    
    // Create a mock Field
    Field field = mock(Field.class);

    // Create a mock ParameterizedType
    ParameterizedType parameterizedType = mock(ParameterizedType.class);

    // Set up the mock Field to return the mock ParameterizedType for getGenericType()
    when(field.getGenericType()).thenReturn(parameterizedType);

    // Create a mock Type for the actual type argument
    Type actualTypeArgument = mock(Type.class);

    // Set up the mock ParameterizedType to return the mock Type for actual type arguments
    when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] { actualTypeArgument });

    // Call the getGenericType() method and assert the result
    Class result = genericMaster.getGenericType(field);
    assertEquals(actualTypeArgument, result);
  }
  
  @Test
  public void testGetGenericType_NestedGenericField() {
    // Create a new instance of GenericMaster
    GenericMaster genericMaster = new GenericMaster();
    
    // Create a mock Field
    Field field = mock(Field.class);

    // Create a mock ParameterizedType
    ParameterizedType parameterizedType = mock(ParameterizedType.class);

    // Set up the mock Field to return the mock ParameterizedType for getGenericType()
    when(field.getGenericType()).thenReturn(parameterizedType);

    // Create a mock Type for the actual type argument of the nested generic
    Type nestedActualTypeArgument = mock(Type.class);

    // Create a mock Type for the nested generic
    ParameterizedType nestedParameterizedType = mock(ParameterizedType.class);

    // Set up the mock ParameterizedType to return the mock Type for the actual type argument
    when(nestedParameterizedType.getActualTypeArguments()).thenReturn(new Type[] { nestedActualTypeArgument });

    // Set up the mock Field to return the mock ParameterizedType for getGenericType() of the nested generic
    when(nestedActualTypeArgument.getGenericType()).thenReturn(nestedParameterizedType);

    // Call the getGenericType() method and assert the result
    Class result = genericMaster.getGenericType(field);
    assertEquals(nestedActualTypeArgument, result);
  }

  // Add more test methods to comprehensively test all methods in GenericMaster class

}