import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstructor() {
        Class<?> baseType = String.class;
        TypeIdResolver idResolver = new TypeIdResolver() {
            // implement the TypeIdResolver interface methods
        };
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Integer.class;

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals(baseType, typeDeserializerBase.baseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerBase.isTypeIdVisible());
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
    }
}