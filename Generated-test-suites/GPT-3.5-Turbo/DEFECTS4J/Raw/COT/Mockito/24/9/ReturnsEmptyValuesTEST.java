
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
    MockUtil mockUtil = new MockUtil();

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public MockName getMockName() {
                return null;
            }

            @Override
            public void setSequenceNumber(int sequenceNumber) {

            }

            @Override
            public int getSequenceNumber() {
                return 0;
            }

            @Override
            public Object[] getArguments() {
                return new Object[0];
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public Class<?> getRawReturnType() {
                return null;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public void markStubbed() {

            }

            @Override
            public String getMethodName() {
                return null;
            }

            @Override
            public Object getMockSettings() {
                return null;
            }

            @Override
            public void setArgumentToCapture(int argumentPosition, Object argument) {

            }

            @Override
            public Object getArgumentToCapture(int argumentPosition) {
                return null;
            }

            @Override
            public void useConstructor(Object... constructorArgs) {

            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }

            @Override
            public boolean isToStringMethod() {
                return false;
            }
        };

        String result = returnsEmptyValues.answer(invocation).toString();
        assertTrue(result.contains("Mock"));
        assertTrue(result.contains("hashCode"));
    }

    @Test
    public void testCompareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public MockName getMockName() {
                return null;
            }

            @Override
            public void setSequenceNumber(int sequenceNumber) {

            }

            @Override
            public int getSequenceNumber() {
                return 0;
            }

            @Override
            public Object[] getArguments() {
                return new Object[0];
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public Class<?> getRawReturnType() {
                return null;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public void markStubbed() {

            }

            @Override
            public String getMethodName() {
                return null;
            }

            @Override
            public Object getMockSettings() {
                return null;
            }

            @Override
            public void setArgumentToCapture(int argumentPosition, Object argument) {

            }

            @Override
            public Object getArgumentToCapture(int argumentPosition) {
                return null;
            }

            @Override
            public void useConstructor(Object... constructorArgs) {

            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }

            @Override
            public boolean isToStringMethod() {
                return false;
            }
        };

        int result = (int) returnsEmptyValues.answer(invocation);
        assertEquals(1, result);
    }

    @Test
    public void testReturnValueFor_Primitives() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class), 0.0);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class), 0.0);
        assertEquals('\u0000', returnsEmptyValues.returnValueFor(char.class));
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));
        assertEquals(null, returnsEmptyValues.returnValueFor(Void.class));
    }

    @Test
    public void testReturnValueFor_Collections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertTrue(returnsEmptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(returnsEmptyValues.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);
    }

    @Test
    public void testReturnValueFor_Maps() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertTrue(returnsEmptyValues.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
    }
}
