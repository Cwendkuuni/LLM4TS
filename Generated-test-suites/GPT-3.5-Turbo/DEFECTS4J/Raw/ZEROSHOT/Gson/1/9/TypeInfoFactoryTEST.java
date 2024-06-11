import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import com.google.gson.Preconditions;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type(){};
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Class<?> typeDefiningF = TypeInfoFactory.class;
        Field f = typeDefiningF.getDeclaredField("typeDefiningF");
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
        assertNotNull(result);
    }

    @Test
    public void testGetActualType() {
        Type typeToEvaluate = new Type(){};
        Type parentType = new Type(){};
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(result);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{};
        Type parentType = new Type(){};
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(result);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = new TypeVariable<?>[]{};
        TypeVariable<?> type = new TypeVariable<?>(){};
        try {
            int result = TypeInfoFactory.getIndex(types, type);
        } catch (IllegalStateException e) {
            assertNotNull(e);
        }
    }
}