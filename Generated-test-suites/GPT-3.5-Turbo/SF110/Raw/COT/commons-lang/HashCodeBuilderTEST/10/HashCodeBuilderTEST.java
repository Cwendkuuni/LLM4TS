package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        // test with null object
        int expected1 = 0;
        int result1 = HashCodeBuilder.reflectionHashCode(null);
        assertEquals(expected1, result1);

        // test with non-null object
        Object object = new Object();
        int expected2 = object.hashCode();
        int result2 = HashCodeBuilder.reflectionHashCode(object);
        assertEquals(expected2, result2);

        // test with exclude fields
        Integer field1 = 10;
        String field2 = "test";
        int field3 = 5;
        TestObject testObject = new TestObject();
        testObject.setField1(field1);
        testObject.setField2(field2);
        testObject.setField3(field3);
        int expected3 = field1.hashCode();
        int result3 = HashCodeBuilder.reflectionHashCode(testObject, "field2", "field3");
        assertEquals(expected3, result3);
    }

    @Test
    public void testReflectionHashCode_withTransients() {
        // test with null object
        int expected1 = 0;
        int result1 = HashCodeBuilder.reflectionHashCode(null, true);
        assertEquals(expected1, result1);

        // test with non-null object
        Object object = new Object();
        int expected2 = object.hashCode();
        int result2 = HashCodeBuilder.reflectionHashCode(object, true);
        assertEquals(expected2, result2);

        // test with exclude fields and transients
        Integer field1 = 10;
        String field2 = "test";
        int field3 = 5;
        TestObject testObject = new TestObject();
        testObject.setField1(field1);
        testObject.setField2(field2);
        testObject.setField3(field3);
        int expected3 = field1.hashCode();
        int result3 = HashCodeBuilder.reflectionHashCode(testObject, true, null, "field3");
        assertEquals(expected3, result3);
    }

    @Test
    public void testReflectionHashCode_withTransientsAndSuper() {
        // test with null object
        int expected1 = 0;
        int result1 = HashCodeBuilder.reflectionHashCode(null, true, TestObject.class);
        assertEquals(expected1, result1);

        // test with non-null object
        Object object = new Object();
        int expected2 = object.hashCode();
        int result2 = HashCodeBuilder.reflectionHashCode(object, true, TestObject.class);
        assertEquals(expected2, result2);

        // test with exclude fields, transients and super classes
        Integer field1 = 10;
        String field2 = "test";
        int field3 = 5;
        TestObject2 testObject = new TestObject2();
        testObject.setField1(field1);
        testObject.setField2(field2);
        testObject.setField3(field3);
        testObject.setField4(15);
        int expected3 = field1.hashCode();
        int result3 = HashCodeBuilder.reflectionHashCode(testObject, true, TestObject.class, "field3");
        assertEquals(expected3, result3);
    }

    @Test
    public void testReflectionHashCode_withCollectionExcludeFields() {
        // test with null object
        int expected1 = 0;
        int result1 = HashCodeBuilder.reflectionHashCode(null, (Collection<String>) null);
        assertEquals(expected1, result1);

        // test with non-null object
        Object object = new Object();
        int expected2 = object.hashCode();
        int result2 = HashCodeBuilder.reflectionHashCode(object, (Collection<String>) null);
        assertEquals(expected2, result2);

        // test with exclude fields as collection
        Integer field1 = 10;
        String field2 = "test";
        int field3 = 5;
        List<String> excludeFields = Arrays.asList("field2", "field3");
        TestObject testObject = new TestObject();
        testObject.setField1(field1);
        testObject.setField2(field2);
        testObject.setField3(field3);
        int expected3 = field1.hashCode();
        int result3 = HashCodeBuilder.reflectionHashCode(testObject, excludeFields);
        assertEquals(expected3, result3);
    }

    @Test
    public void testHashCode() {
        // create test object
        TestObject testObject = new TestObject();
        testObject.setField1(10);
        testObject.setField2("test");
        testObject.setField3(5);

        // calculate expected hash code
        int expected = 17 * 37 * 37 + testObject.getField1().hashCode() * 37 + testObject.getField2().hashCode();

        // calculate actual hash code
        int result = testObject.hashCode();

        assertEquals(expected, result);
    }

    @Test
    public void testHashCode_withSuper() {
        // create test object
        TestObject2 testObject = new TestObject2();
        testObject.setField1(10);
        testObject.setField2("test");
        testObject.setField3(5);
        testObject.setField4(15);

        // calculate expected hash code
        int expected = 17 * 37 * 37 + testObject.getField1().hashCode() * 37 + testObject.getField2().hashCode() + testObject.getField4();

        // calculate actual hash code
        int result = testObject.hashCode();

        assertEquals(expected, result);
    }

    class TestObject {

        private Integer field1;
        private String field2;
        private int field3;

        public Integer getField1() {
            return field1;
        }

        public void setField1(Integer field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        public int getField3() {
            return field3;
        }

        public void setField3(int field3) {
            this.field3 = field3;
        }
    }

    class TestObject2 extends TestObject {

        private int field4;

        public int getField4() {
            return field4;
        }

        public void setField4(int field4) {
            this.field4 = field4;
        }
    }
}