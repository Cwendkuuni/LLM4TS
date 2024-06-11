
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import java.util.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

  ReturnsEmptyValues returnsEmpty = new ReturnsEmptyValues();

  @Test
  public void testAnswerToString() {
    String methodName = "toString";
    Class<?>[] parameterTypes = {};
    Object returnValue = returnsEmpty.answer(new InvocationOnMock() {
      @Override
      public Object getMock() {
        return null;
      }

      @Override
      public MockName getMockName() {
        return null;
      }

      @Override
      public Object[] getArguments() {
        return null;
      }

      @Override
      public Method getMethod() {
        try {
          return ReturnsEmptyValues.class.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
        return null;
      }
    });
    String expectedValue = "Mock for ReturnsEmptyValues, hashCode: 0";
    assertEquals(expectedValue, returnValue);
  }

  @Test
  public void testAnswerCompareTo() {
    String methodName = "compareTo";
    Class<?>[] parameterTypes = {};
    Object returnValue = returnsEmpty.answer(new InvocationOnMock() {
      @Override
      public Object getMock() {
        return null;
      }

      @Override
      public MockName getMockName() {
        return null;
      }

      @Override
      public Object[] getArguments() {
        return null;
      }

      @Override
      public Method getMethod() {
        try {
          return ReturnsEmptyValues.class.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
        return null;
      }
    });
    int expectedValue = 1;
    assertEquals(expectedValue, returnValue);
  }

  @Test
  public void testAnswerForPrimitives() {
    List<Class<?>> primitiveTypes = Arrays.asList(
        boolean.class, byte.class, short.class, int.class, long.class, float.class, double.class, char.class
    );

    for (Class<?> type : primitiveTypes) {
      Object returnValue = returnsEmpty.returnValueFor(type);
      assertEquals(0, returnValue);
    }
  }

  @Test
  public void testAnswerForPrimitiveWrappers() {
    List<Class<?>> wrapperTypes = Arrays.asList(
        Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Character.class
    );

    for (Class<?> type : wrapperTypes) {
      Object returnValue = returnsEmpty.returnValueFor(type);
      assertEquals(0, returnValue);
    }
  }

  @Test
  public void testAnswerForCollections() {
    List<Class<?>> collectionTypes = Arrays.asList(
        Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class,
        List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class,
        TreeMap.class, LinkedHashMap.class
    );

    for (Class<?> type : collectionTypes) {
      Object returnValue = returnsEmpty.returnValueFor(type);
      assertNotNull(returnValue);
      assertTrue(returnValue instanceof Collection);
      assertTrue(((Collection) returnValue).isEmpty());
    }
  }

}
