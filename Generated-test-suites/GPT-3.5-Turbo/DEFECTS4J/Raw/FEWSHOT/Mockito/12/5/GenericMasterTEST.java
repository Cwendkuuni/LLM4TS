import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();

        // Test for non-generic field
        Field nonGenericTypeField = mock(Field.class);
        Type nonGenericType = null;
        assertEquals(Object.class, genericMaster.getGenericType(nonGenericTypeField));

        // Test for generic field
        Field genericTypeField = mock(Field.class);
        Type genericType = mock(ParameterizedType.class);
        Type actualTypeArgument = mock(Class.class);
        ((ParameterizedType) genericType).getActualTypeArguments()[0] = actualTypeArgument;
        assertEquals((Class) actualTypeArgument, genericMaster.getGenericType(genericTypeField));
    }

}