import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;
    private Field field;

    @Before
    public void setUp() throws NoSuchFieldException {
        genericMaster = new GenericMaster();
        field = MockedClass.class.getDeclaredField("mockedField");
    }

    @Test
    public void testGetGenericType_WithGenericType_ShouldReturnCorrectType() {
        Type genericType = mock(ParameterizedType.class);
        when(field.getGenericType()).thenReturn(genericType);
        when(((ParameterizedType) genericType).getActualTypeArguments()[0]).thenReturn(String.class);

        Class expectedResult = String.class;
        Class actualResult = genericMaster.getGenericType(field);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetGenericType_WithoutGenericType_ShouldReturnObjectClass() {
        Type genericType = null;
        when(field.getGenericType()).thenReturn(genericType);

        Class expectedResult = Object.class;
        Class actualResult = genericMaster.getGenericType(field);

        assertEquals(expectedResult, actualResult);
    }

    private static class MockedClass {
        private List<String> mockedField;
    }
}