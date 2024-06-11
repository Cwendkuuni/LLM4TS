import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.*;

import com.google.gson.internal.UnsafeAllocator;

@Category(IntegrationTests.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @Before
 public void setUp() throws Exception {
 allocator = UnsafeAllocator.create();
 }

 /**
 * Test newInstance method for a simple Java class
 */
 @Test
 public void testNewInstanceForSimpleClass() {
 try {
 Class<Integer> intType = Integer.TYPE;
 Integer inst = allocator.newInstance(intType);
 assertEquals(0, inst.intValue());
 } catch (Exception e) {
 fail("Exception thrown while testing newInstance for Integer: " + e.getMessage());
 }
 }

 /**
 * Test newInstance method for a custom class with no constructors
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForCustomClassWithNoConstructors() {
 try {
 Class<NoConstructors> noConstructorsType = NoConstructors.class;
 allocator.newInstance(noConstructorsType);
 fail("Exception not thrown for NoConstructors class");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 /**
 * Test newInstance method for a custom interface
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForCustomInterface() {
 try {
 Class<MyInterface> myInterfaceType = MyInterface.class;
 allocator.newInstance(myInterfaceType);
 fail("Exception not thrown for MyInterface");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 /**
 * Test newInstance method for a custom abstract class
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForCustomAbstractClass() {
 try {
 Class<MyAbstractClass> myAbstractClassType = MyAbstractClass.class;
 allocator.newInstance(myAbstractClassType);
 fail("Exception not thrown for MyAbstractClass");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 /**
 * Test newInstance method for a Java class with a private constructor
 * (allocation through reflection should be possible)
 */
 @Test
 public void testNewInstanceForPrivateConstructorJavaClass() {
 try {
 Class<PrivateConstructor> privateConstructorType = PrivateConstructor.class;
 PrivateConstructor inst = (PrivateConstructor) allocator.newInstance(privateConstructorType);
 assertNotNull(inst);
 } catch (Exception e) {
 fail("Exception thrown while testing newInstance for PrivateConstructor: " + e.getMessage());
 }
 }

 /**
 * Test newInstance method for a Java class that throws exception in constructor
 */
 @Test(expected = RuntimeException.class)
 public void testNewInstanceForJavaClassWithExceptionInConstructor() {
 try {
 Class<ExceptionInConstructor> exceptionInConstructorType = ExceptionInConstructor.class;
 allocator.newInstance(exceptionInConstructorType);
 fail("Exception not thrown for ExceptionInConstructor");
 } catch (Exception e) {
 // Expected
 throw new RuntimeException("Expected exception in constructor not thrown: " + e.getMessage(), e);
 }
 }

 /**
 * Test newInstance method for a Java class with final class modifier
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForFinalJavaClass() {
 try {
 Class<FinalClass> finalClassType = FinalClass.class;
 allocator.newInstance(finalClassType);
 fail("Exception not thrown for FinalClass");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 /**
 * Test newInstance method for a Java class with private nested classes
 */
 @Test
 public void testNewInstanceForJavaClassWithPrivateNestedClasses() {
 try {
 Class<NestedClass> nestedClassType = NestedClass.class;
 NestedClass inst = (NestedClass) allocator.newInstance(nestedClassType);
 assertNotNull(inst);
 } catch (Exception e) {
 fail("Exception thrown while testing newInstance for NestedClass: " + e.getMessage());
 }
 }
}

/**
 * Custom class with no constructors
 */
class NoConstructors {}

/**
 * Custom interface
 */
interface MyInterface {}

/**
 * Custom abstract class
 */
abstract class MyAbstractClass {}

/**
 * Java class with a private constructor
 */
class PrivateConstructor {
 private PrivateConstructor() {}
}

/**
 * Java class that throws exception in constructor
 */
class ExceptionInConstructor {
 public ExceptionInConstructor() throws Exception {
 throw new RuntimeException("Exception in constructor");
 }
}

/**
 * Java class with final class modifier
 */
final class FinalClass {}

/**
 * Nested class
 */
class NestedClass {
 private NestedClass() {}
}