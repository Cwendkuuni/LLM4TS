import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ValuedEnumTest {

    private Class<ValuedEnum> enumClass;
    private String enumName;
    private int expectedValue;

    public ValuedEnumTest(Class<ValuedEnum> enumClass, String enumName, int expectedValue) {
        this.enumClass = enumClass;
        this.enumName = enumName;
        this.expectedValue = expectedValue;
    }

    @Parameterized.Parameters(name = "{index}: {0} has value {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {JavaVersionEnum.class, "JAVA1_0", 100},
                {JavaVersionEnum.class, "JAVA1_1", 110},
                {JavaVersionEnum.class, "JAVA1_2", 120},
                {JavaVersionEnum.class, "JAVA1_3", 130}
        });
    }

    @Test
    public void testGetEnum() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ValuedEnum enumValue = EnumUtils.getEnum(enumClass, enumName);
        assertEquals(enumName, enumValue.getName());
        assertEquals(expectedValue, enumValue.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullEnumClass() {
        EnumUtils.getEnum(null, "JAVA1_0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullName() {
        EnumUtils.getEnum(enumClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidName() {
        EnumUtils.getEnum(enumClass, "INVALID_ENUM");
    }

    @Test
    public void testGetEnumMap() {
        Map<String, ValuedEnum> enumMap = EnumUtils.getEnumMap(enumClass);
        assertEquals(4, enumMap.size());
        assertNotNull(enumMap.get(enumName));
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = EnumUtils.getEnumList(enumClass);
        assertEquals(4, enumList.size());
        assertTrue(enumList.contains(EnumUtils.getEnum(enumClass, enumName)));
    }

    @Test
    public void testGetEnumValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getValueMethod = MethodUtils.getAccessibleMethod(ValuedEnum.class, "getValue");
        ValuedEnum enumValue = EnumUtils.getEnum(enumClass, enumName);
        int actualValue = (int) getValueMethod.invoke(enumValue);
        assertEquals(expectedValue, actualValue);
    }
}