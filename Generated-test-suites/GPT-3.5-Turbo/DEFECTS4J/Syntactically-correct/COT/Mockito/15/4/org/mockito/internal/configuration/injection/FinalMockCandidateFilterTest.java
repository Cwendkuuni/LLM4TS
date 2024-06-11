import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.exceptions.base.MockitoException;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

   @Test
   public void testFilterCandidateSingleMock() {
      FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
      Collection<Object> mocks = new Collection<Object>() {
         public int size() {
            return 1;
         }

         public boolean isEmpty() {
            return false;
         }

         public boolean contains(Object o) {
            return true;
         }

         public Iterator<Object> iterator() {
            return new Iterator<Object>() {
               public boolean hasNext() {
                  return true;
               }

               public Object next() {
                  return mock(Object.class);
               }

               public void remove() {
                  // Do nothing
               }
            };
         }

         public Object[] toArray() {
            return null;
         }

         public <T> T[] toArray(T[] a) {
            return null;
         }

         public boolean add(Object o) {
            return false;
         }

         public boolean remove(Object o) {
            return false;
         }

         public boolean containsAll(Collection<?> c) {
            return false;
         }

         public boolean addAll(Collection<? extends Object> c) {
            return false;
         }

         public boolean removeAll(Collection<?> c) {
            return false;
         }

         public boolean retainAll(Collection<?> c) {
            return false;
         }

         public void clear() {
            // Do nothing
         }
      };
      Field field = mock(Field.class);
      Object fieldInstance = mock(Object.class);
      
      OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
      
      assertTrue(result.thenInject());
   }

   @Test
   public void testFilterCandidateMultipleMocks() {
      FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
      Collection<Object> mocks = new Collection<Object>() {
         public int size() {
            return 2;
         }

         public boolean isEmpty() {
            return false;
         }

         public boolean contains(Object o) {
            return true;
         }

         public Iterator<Object> iterator() {
            return new Iterator<Object>() {
               public boolean hasNext() {
                  return true;
               }

               public Object next() {
                  return mock(Object.class);
               }

               public void remove() {
                  // Do nothing
               }
            };
         }

         public Object[] toArray() {
            return null;
         }

         public <T> T[] toArray(T[] a) {
            return null;
         }

         public boolean add(Object o) {
            return false;
         }

         public boolean remove(Object o) {
            return false;
         }

         public boolean containsAll(Collection<?> c) {
            return false;
         }

         public boolean addAll(Collection<? extends Object> c) {
            return false;
         }

         public boolean removeAll(Collection<?> c) {
            return false;
         }

         public boolean retainAll(Collection<?> c) {
            return false;
         }

         public void clear() {
            // Do nothing
         }
      };
      Field field = mock(Field.class);
      Object fieldInstance = mock(Object.class);
      
      OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
      
      assertFalse(result.thenInject());
   }

   @Test(expected = MockitoException.class)
   public void testFilterCandidateException() {
      FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
      Collection<Object> mocks = new Collection<Object>() {
         public int size() {
            return 1;
         }

         public boolean isEmpty() {
            return false;
         }

         public boolean contains(Object o) {
            return true;
         }

         public Iterator<Object> iterator() {
            return new Iterator<Object>() {
               public boolean hasNext() {
                  return true;
               }

               public Object next() {
                  return mock(Object.class);
               }

               public void remove() {
                  // Do nothing
               }
            };
         }

         public Object[] toArray() {
            return null;
         }

         public <T> T[] toArray(T[] a) {
            return null;
         }

         public boolean add(Object o) {
            return false;
         }

         public boolean remove(Object o) {
            return false;
         }

         public boolean containsAll(Collection<?> c) {
            return false;
         }

         public boolean addAll(Collection<? extends Object> c) {
            return false;
         }

         public boolean removeAll(Collection<?> c) {
            return false;
         }

         public boolean retainAll(Collection<?> c) {
            return false;
         }

         public void clear() {
            // Do nothing
         }
      };
      Field field = mock(Field.class);
      Object fieldInstance = mock(Object.class);

      // Throw exception
      when(field.getName()).thenThrow(new RuntimeException("Exception"));

      filter.filterCandidate(mocks, field, fieldInstance);
   }
}