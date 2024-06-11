import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeInfoFactoryTest {
    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(Object[].class);
        assertEquals(Object[].class, typeInfo.getType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayWithNonArrayClass() {
        TypeInfoFactory.getTypeInfoForArray(Object.class);
    }
    
    @Test
    public void testGetTypeInfoForField() {
        // Test case for actual type being a Class
        TypeInfo typeInfo1 = TypeInfoFactory.getTypeInfoForField(null, String.class);
        assertEquals(String.class, typeInfo1.getType());
        
        // Test case for actual type being a ParameterizedType
        TypeInfo typeInfo2 = TypeInfoFactory.getTypeInfoForField(null, TestClass.class);
        assertEquals(TestClass.class, typeInfo2.getType());
    }
    
    @Test
    public void testGetActualTypeWithClass() {
        Type actualType1 = TypeInfoFactory.getActualType(String.class, null, null);
        assertEquals(String.class, actualType1);
    }
    
    @Test
    public void testGetActualTypeWithParameterizedType() {
        ParameterizedType parameterizedType = new TestParameterizedType();
        Type actualType = TypeInfoFactory.getActualType(parameterizedType, null, null);
        assertEquals(TestParameterizedType.class, actualType);
    }
    
    @Test
    public void testGetActualTypeWithGenericArrayType() {
        GenericArrayType genericArrayType = new TestGenericArrayType();
        Type actualType = TypeInfoFactory.getActualType(genericArrayType, null, null);
        assertEquals(TestGenericArrayType.class, actualType);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualTypeWithTypeVariableWithoutParameterizedType() {
        TypeVariable<Class<TestParameterizedType>> typeVariable = TestParameterizedType.class.getTypeParameters()[0];
        TypeInfoFactory.getActualType(typeVariable, null, null);
    }
    
    @Test
    public void testGetActualTypeWithTypeVariableWithParameterizedType() {
        TypeVariable<Class<TestParameterizedType>> typeVariable = TestParameterizedType.class.getTypeParameters()[0];
        ParameterizedType parameterizedType = new TestParameterizedType();
        Type actualType = TypeInfoFactory.getActualType(typeVariable, parameterizedType, TestParameterizedType.class);
        assertEquals(TestParameterizedType.class, actualType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeWithUnknownType() {
        TypeInfoFactory.getActualType(null, null, null);
    }
    
    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[] {String.class, Integer.class};
        Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, null, null);
        
        assertEquals(String.class, retTypes[0]);
        assertEquals(Integer.class, retTypes[1]);
    }
    
    @Test
    public void testGetIndex() {
        TypeVariable<Class<TestParameterizedType>> typeVariable1 = TestParameterizedType.class.getTypeParameters()[0];
        TypeVariable<Class<TestParameterizedType>> typeVariable2 = TestParameterizedType.class.getTypeParameters()[1];
        
        TypeVariable<Class<TestParameterizedType>>[] types = new TypeVariable[] { typeVariable1, typeVariable2 };
        
        int index1 = TypeInfoFactory.getIndex(types, typeVariable1);
        int index2 = TypeInfoFactory.getIndex(types, typeVariable2);
        
        assertEquals(0, index1);
        assertEquals(1, index2);
    }
}