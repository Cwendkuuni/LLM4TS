package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumClass = EnumTestHelper.createTestEnum(ValuedEnum.class);
        ValuedEnum value1 = ValuedEnum.getEnum(enumClass, 1);
        ValuedEnum value2 = ValuedEnum.getEnum(enumClass, 2);

        assertEquals(Value1.class, value1.getClass());
        assertEquals(Value1.NAME, value1.getName());
        assertEquals(1, value1.getValue());

        assertEquals(Value2.class, value2.getClass());
        assertEquals(Value2.NAME, value2.getName());
        assertEquals(2, value2.getValue());

        assertNull(ValuedEnum.getEnum(enumClass, 0));
        assertNull(ValuedEnum.getEnum(enumClass, 3));
    }

    @Test
    public void testGetEnumWithReflection() throws Exception {
        ValuedEnum enumClass = EnumTestHelper.createTestEnum(ValuedEnum.class);
        Constructor<ValuedEnum> constructor = enumClass.getConstructor(String.class, int.class);
        ValuedEnum value1 = constructor.newInstance("Value1", 1);
        ValuedEnum value2 = constructor.newInstance("Value2", 2);

        assertEquals(Value1.class, value1.getClass());
        assertEquals(Value1.NAME, value1.getName());
        assertEquals(1, value1.getValue());

        assertEquals(Value2.class, value2.getClass());
        assertEquals(Value2.NAME, value2.getName());
        assertEquals(2, value2.getValue());

        ValuedEnum value3 = (ValuedEnum) constructor.newInstance("Value3", 3);
        assertEquals(Value3.class, value3.getClass());
        assertEquals(Value3.NAME, value3.getName());
        assertEquals(3, value3.getValue());

        assertNull(constructor.newInstance(null, 0));
        assertNull(constructor.newInstance(null, 3));
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enumClass = EnumTestHelper.createTestEnum(ValuedEnum.class);
        List<ValuedEnum> enumList = Arrays.asList(enumClass.getEnumConstants());
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);

        assertEquals(enumList.size(), enumMap.size());
        for (ValuedEnum value : enumList) {
            assertEquals(value, enumMap.get(value.getName()));
        }
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enumClass = EnumTestHelper.createTestEnum(ValuedEnum.class);
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);

        assertEquals(enumList.size(), enumClass.getEnumConstants().length);
        for (ValuedEnum value : enumList) {
            assertEquals(value, enumClass.getEnumConstants()[enumClass.getEnumConstants().length - enumList.indexOf(value) - 1]);
        }
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enumClass = EnumTestHelper.createTestEnum(ValuedEnum.class);
        ValuedEnum value1 = ValuedEnum.getEnum(enumClass, 1);
        ValuedEnum value2 = ValuedEnum.getEnum(enumClass, 2);
        ValuedEnum value3 = ValuedEnum.getEnum(enumClass, 3);

        assertEquals(0, value1.compareTo(value1));
        assertEquals(-1, value1.compareTo(value2));
        assertEquals(1, value2.compareTo(value1));
        assertEquals(0, value2.compareTo(value2));
        assertEquals(-1, value2.compareTo(value3));
        assertEquals(1, value3.compareTo(value2));
        assertEquals(0, value3.compareTo(value3));
    }

    @Test
    public void testCompareToDifferentClass() {
        ValuedEnum enum1 = EnumTestHelper.createTestEnum(ValuedEnum1.class);
        ValuedEnum enum2 = EnumTestHelper.createTestEnum(ValuedEnum2.class);

        assertEquals(0, enum1.compareTo(enum1));
        assertEquals(Integer.MIN_VALUE, enum1.compareTo(enum2));
        assertEquals(Integer.MAX_VALUE, enum2.compareTo(enum1));
        assertEquals(0, enum2.compareTo(enum2));
    }

    @Test
    public void testToString() {
        ValuedEnum enumClass = EnumTestHelper.createTestEnum(ValuedEnum.class);
        ValuedEnum value1 = ValuedEnum.getEnum(enumClass, 1);
        ValuedEnum value2 = ValuedEnum.getEnum(enumClass, 2);

        assertEquals("ValuedEnum[Value1=1]", value1.toString());
        assertEquals("ValuedEnum[Value2=2]", value2.toString());
    }

    private static class EnumTestHelper {
        public static <T extends Enum> Class<T> createTestEnum(Class<T> enumClass) {
            if (enumClass == null) {
                throw new IllegalArgumentException("Enum class cannot be null.");
            }

            if (!Enum.isEnum(enumClass)) {
                throw new IllegalArgumentException("Given class is not an Enum: " + enumClass.getName());
            }

            try {
                Constructor<T> constructor = enumClass.getDeclaredConstructor();
                constructor.setAccessible(true);

                Method method = enumClass.getMethod("values");
                method.setAccessible(true);

                return enumClass;
            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalStateException("Unable to reflectively construct Enum values from the given class.", e);
            }
        }
    }

    @SuppressWarnings("unused")
    private static class Value1 extends ValuedEnum {
        public static final ValuedEnum VALUE1 = new Value1();

        private Value1() {
            super("Value1", 1);
        }

        private static final long serialVersionUID = 1L;

        @Override
        public String getName() {
            return NAME;
        }
    }

    @SuppressWarnings("unused")
    private static class Value2 extends ValuedEnum {
        public static final ValuedEnum VALUE2 = new Value2();

        private Value2() {
            super("Value2", 2);
        }

        private static final long serialVersionUID = 1L;

        @Override
        public String getName() {
            return NAME;
        }
    }

    @SuppressWarnings("unused")
    private static class Value3 extends ValuedEnum {
        public static final ValuedEnum VALUE3 = new Value3();

        private Value3() {
            super("Value3", 3);
        }

        private static final long serialVersionUID = 1L;

        @Override
        public String getName() {
            return NAME;
        }
    }

    private static class ValuedEnum1 extends ValuedEnum {
        public static final ValuedEnum VALUE1_1 = new ValuedEnum1();

        private ValuedEnum1() {
            super("ValuedEnum1_Value1", 11);
        }

        private static final long serialVersionUID = 1L;
    }

    private static class ValuedEnum2 extends ValuedEnum {
        public static final ValuedEnum VALUE1_2 = new ValuedEnum2();

        private ValuedEnum2() {
            super("ValuedEnum2_Value1", 22);
        }

        private static final long serialVersionUID = 1L;
    }

    private static Map<String, ValuedEnum> map;
    private static List<ValuedEnum> list;
    private static Iterator<ValuedEnum> iterator;

    static {
        try {
            map = (Map<String, ValuedEnum>) Class.forName("java.util.Collections")
                    .getMethod("unmodifiableMap")
                    .invoke(null);

            list = Arrays.asList(new ValuedEnum[] { new Value1(), new Value2() });

            Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
            Constructor<? extends ValuedEnum> constructor = enumClass.getDeclaredConstructor(String.class, int.class);
            constructor.setAccessible(true);
            iterator = Arrays.asList(new ValuedEnum[] { new Value1(), new Value2() })
                    .listIterator();
        } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}