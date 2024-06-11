import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstance_WithParameterlessConstructor() {
 ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
 Class<T> cls = (Class<T>) String.class;
 T instance = instantIator.newInstance(cls);
 Assert.assertNotNull(instance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testNewInstance_WithNoParameterlessConstructor_ThrowException() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 Class<T> cls = (Class<T>) InvalidClass.class;
 instantiator.newInstance(cls);
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testWithOuterClass_WithParameterlessConstructor() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
 Class<T> cls = (Class<T>) String.class;
 T instance = instantIator.newInstance(cls);
 Assert.assertNotNull(instance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClass_WithNoParameterlessConstructor_ThrowException() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
 Class<T> cls = (Class<T>) InvalidClass.class;
 instantIator.newInstance(cls);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClass_WithException_ThrowParamsException() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
 Class<T> cls = (Class<T>) ExceptionClassWithParameters.class;
 try {
 instantIator.newInstance(cls);
 } catch (InstantiationException e) {
 Assert.assertEquals("Unable to create mock instance of 'ExceptionClassWithParameters'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testWithOuterClass_WithNoDeclaredConstructor_ThrowParamsException() {
 outerClassInstance = new Object();
 ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
 Class<T> cls = (Class<T>) NonExistentClass.class;
 try {
 instantIator.newInstance(cls);
 } catch (InstantiationException e) {
 Assert.assertEquals("Unable to create mock instance of 'NonExistentClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNoArgConstructor_WithParameterlessConstructor() {
 Class<T> cls = (Class<T>) String.class;
 T instance = ConstructorInstantiator.noArgConstructor(cls);
 Assert.assertNotNull(instance);
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructor_WithNoParameterlessConstructor_ThrowException() {
 Class<InvalidClass> cls = (Class<InvalidClass>) InvalidClass.class;
 ConstructorInstantiator.noArgConstructor(cls);
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructor_WithException_ThrowException() {
 try {
 ConstructorInstantiator.noArgConstructor(String[].class);
 } catch (InstantiationException e) {
 Assert.assertEquals("Unable to create mock instance of '[Ljava.lang.String;'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
 }
 }

 @Test(expected = InstantiationException.class)
 public void testNoArgConstructor_WithArrayClass_ThrowException() {
 try {
 ConstructorInstantiator.noArgConstructor(int[].class);
 } catch (InstantiationException e) {
 Assert.assertEquals("Unable to create mock instance of '[I'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
 }
 }

 @Test
 public void testParamsException_WithExceptionMessage() {
 try {
 throw new InstantiationException("custom message");
 } catch (InstantiationException e) {
 Assert.assertEquals("custom message", e.getMessage());
 }
 }

 @Test
 public void testParamsException_WithException() {
 Class<ExceptionClassWithParameters> cls = (Class<ExceptionClassWithParameters>) ExceptionClassWithParameters.class;
 try {
 ConstructorInstantiator.paramsException(cls, new InstantiationException("custom message", new Exception()));
 } catch (InstantiationException e) {
 Assert.assertEquals("Unable to create mock instance of 'ExceptionClassWithParameters'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
 }
 }

}