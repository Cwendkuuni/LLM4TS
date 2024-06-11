import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator unsafeAllocator;

 @Before
 public void setUp() {
 unsafeAllocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstance_Object() {
 Object obj = unsafeAllocator.newInstance(Object.class);
 assertNotNull(obj);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Array() {
 try {
 unsafeAllocator.newInstance(int[].class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 unsafeAllocator.newInstance(int[].class);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Interface() {
 try {
 unsafeAllocator.newInstance(Runnable.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_AbstractClass() {
 try {
 unsafeAllocator.newInstance(AbstractClass.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_ExceptionClass() {
 try {
 unsafeAllocator.newInstance(Exception.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNewInstance_Null() {
 assertNull(unsafeAllocator.newInstance(null));
 }

 @Test(expected = NullPointerException.class)
 public void testNewInstance_NullClass() {
 unsafeAllocator.newInstance((Class<?>) null);
 }

 @Test
 public void testNewInstance_String() {
 String str = (String) unsafeAllocator.newInstance(String.class);
 assertNotNull(str);
 }

 @Test
 public void testNewInstance_CustomClass() {
 try {
 @SuppressWarnings("unused")
 CustomClass customClass = (CustomClass) unsafeAllocator.newInstance(CustomClass.class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 static class CustomClass {
 }

 @Test
 public void testNewInstance_ArrayOfCustomClass() {
 try {
 @SuppressWarnings("unused")
 CustomClass[] customClassArray = (CustomClass[]) unsafeAllocator.newInstance(CustomClass[].class);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @After
 public void tearDown() {
 unsafeAllocator = null;
 }
}