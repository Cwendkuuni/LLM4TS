import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_ReturnsObjectClass_WhenFieldIsNotGeneric() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("nonGenericField");
        Class expected = Object.class;
        Class actual = genericMaster.getGenericType(field);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_ReturnsGenericType_WhenFieldIsGeneric() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("genericField");
        Class expected = DummyGeneric.class;
        Class actual = genericMaster.getGenericType(field);
        assertEquals(expected, actual);
    }

    private static class DummyClass {
        private DummyGeneric<String> genericField;
        private String nonGenericField;
    }

    private static class DummyGeneric<T> {
        private T value;
    }
}