
package org.apache.commons.lang3.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        // Test case 1
        Object lhs1 = new String("abc");
        Object rhs1 = new String("abc");
        assertEquals(0, CompareToBuilder.reflectionCompare(lhs1, rhs1));

        // Test case 2
        Object lhs2 = new Integer(10);
        Object rhs2 = new Integer(20);
        assertEquals(-1, CompareToBuilder.reflectionCompare(lhs2, rhs2));

        // Test case 3
        Object lhs3 = new Double(2.5);
        Object rhs3 = new Double(1.5);
        assertEquals(1, CompareToBuilder.reflectionCompare(lhs3, rhs3));

        // Test case 4
        Object lhs4 = new int[] {1, 2, 3};
        Object rhs4 = new int[] {1, 2, 4};
        assertEquals(-1, CompareToBuilder.reflectionCompare(lhs4, rhs4));
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        // Test case 1
        Object lhs1 = new Person("John", 25);
        Object rhs1 = new Person("John", 30);
        assertEquals(0, CompareToBuilder.reflectionCompare(lhs1, rhs1, "age"));

        // Test case 2
        Object lhs2 = new Person("Jane", 20);
        Object rhs2 = new Person("John", 20);
        assertEquals(0, CompareToBuilder.reflectionCompare(lhs2, rhs2, "name"));
    }

    private static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
