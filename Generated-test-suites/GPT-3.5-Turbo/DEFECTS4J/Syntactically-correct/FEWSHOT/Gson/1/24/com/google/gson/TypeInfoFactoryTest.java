import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(new String[]{"test"});
        assertTrue(info.getType() instanceof GenericArrayType);
    }

    @Test
    public void testGetTypeInfoForField() {
        try {
            Field field = TypeInfoFactory.class.getDeclaredField("classDefiningF");
            TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, TypeInfoFactory.class);
            assertTrue(info.getType() instanceof Class<?>);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetActualType_Class() {
        Type type = TypeInfoFactory.getTypeInfoForField(TypeInfoFactory.class);
        assertTrue(type instanceof Class<?>);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        ParameterizedTypeImpl type = (ParameterizedTypeImpl) TypeInfoFactory.getTypeInfoForField(new Field() {}, ParameterizedTypeImpl.class);
        assertTrue(type.getRawType() instanceof Class<?>);
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        GenericArrayTypeImpl type = (GenericArrayTypeImpl) TypeInfoFactory.getTypeInfoForField(new Field() {}, GenericArrayTypeImpl.class);
        assertTrue(type.getGenericComponentType() instanceof Class<?>);
    }

    @Test
    public void testGetActualType_TypeVariable() {
        ParameterizedTypeImpl typeDefiningF = new ParameterizedTypeImpl(TypeInfoFactory.class, new TypeVariable() {}, TypeVariable.class);
        TypeVariable<?> type = (TypeVariable<?>) TypeInfoFactory.getTypeInfoForField(new Field() {}, typeDefiningF);
        assertTrue(type.getGenericDeclaration() instanceof ParameterizedType);
    }

    @Test
    public void testGetActualType_WildcardType() {
        WildcardType wildcard = (WildcardType) TypeInfoFactory.getTypeInfoForField(new Field() {}, WildcardType.class);
        assertTrue(wildcard.getUpperBounds()[0] instanceof Class<?>);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        TypeInfoFactory.getTypeInfoForField(new Field() {}, Integer.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_TypeVariableNotFound() {
        TypeVariable<?>[] types = {};
        TypeInfoFactory.getIndex(types, new TypeVariable() {});
    }

}