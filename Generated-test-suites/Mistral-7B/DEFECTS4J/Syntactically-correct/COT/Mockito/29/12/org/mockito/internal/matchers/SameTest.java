import org.junit.Test;
import org.junit. Assert;
import org.hamcrest.Matcher;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.internal.matchers.Same.same;

public class SameTest {

 private Object wanted;

 @Test
 public void testSameInstance() {
 wanted = new Object();
 Assert.assertTrue(same(wanted).matches(wanted));
 }

 @Test
 public void testSameClassAndDifferentObjects() {
 wanted = new Object();
 Assert.assertFalse(same(new Object()).matches(wanted));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWanted() {
 wanted = null;
 try {
 new Same(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("wanted cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullActual() {
 try {
 same(wanted).matches(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("actual cannot be null", e.getMessage());
 }
 }

 @Test
 public void testStringArgument() {
 wanted = "hello";
 Assert.assertTrue(same(wanted).matches("hello"));
 }

 @Test
 public void testCharacterArgument() {
 wanted = 'a';
 Assert.assertTrue(same(wanted).matches('a'));
 }

 @Test
 public void testDifferentClasses() {
 wanted = new Object();
 Assert.assertFalse(same(new String("hello")).matches(wanted));
 }

 @Test
 public void testMatchingSubclass() {
 wanted = new A();
 Matcher<Object> matcher = same(wanted);
 Assert.assertTrue(matcher.matches(new A()));
 }

 @Test
 public void testNotMatchingSubclass() {
 wanted = new A();
 Matcher<Object> matcher = same(wanted);
 Assert.assertFalse(matcher.matches(new B()));
 }

 private static class A {
 }

 private static class B {
 }
}