import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private ClassImposterizer classImposterizer;
 private MockHandler<Object> mockHandler;
 private Object spiedInstance;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 classImposterizer = Mockito.spy(ClassImposterizer.INSTANCE);
 mockHandler = Mockito.spy(new MockHandler<>(new MockSettingsImpl()));
 spiedInstance = new Object();
 }

 @Test
 public void testCreateMock() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);

 Object mock = mockUtil.createMock(classToMock, settings);
 assertTrue(isMockitoMock(mock));
 assertNotNull(mockHandler.getMockSettings().getMockName());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 mockUtil.createMock(Object.class, new MockSettingsImpl());
 Mockito.doThrow(new NotAMockException("Argument should be a mock, but is null!"))
 .when(classImposterizer)
 .imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class), ArgumentMatchers.eq(Object.class), ArgumentMatchers.array(new Class[0]));
 mockUtil.createMock(Object.class, new MockSettingsImpl());
 }

 @Test
 public void testResetMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 assertNull(getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockWithMockitoMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptorWithMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test
 public void testGetMockNameWithMock() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }

 @Test
 public void testValidationOfType() {
 try {
 mockUtil.createMock(int[].class, new MockSettingsImpl());
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testValidationOfExtraInterfaces() {
 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setExtraInterfaces(new Class[0]));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setExtraInterfaces(new Class[]{String.class, Integer.class}));
 } catch (IllegalArgumentException e) {
 fail("Should not have thrown exception");
 }
}

 @Test(expected = NotAMockException.class)
 public void testValidationMockedTypeWithNullSpiedInstance() {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setSpiedInstance(null));
 }

 @Test
 public void testValidationMockedTypeWithNullClassToMock() {
 try {
 mockUtil.createMock(null, new MockSettingsImpl());
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testValidationMockedType() {
 try {
 mockUtil.createMock(String.class, new MockSettingsImpl().setSpiedInstance(spiedInstance));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testValidationMockedTypeWithSameClass() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().setSpiedInstance(mock));
 fail("Should have thrown exception");
 }

 @Test
 public void testValidationMockedTypeWithDifferentClassButImplementsSameInterface() {
 Object anotherClass = new Object() {
 @Override
 public void method() {}
 };
 mockUtil.createMock(anotherClass.getClass(), new MockSettingsImpl().setSpiedInstance(spiedInstance));
 }

 @Test
 public void testValidationMockedTypeWithDifferentClassButSameInterface() {
 Object anotherClass = new Object() {
 @Override
 public void method() {}
 };
 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setSpiedInstance(anotherClass).setExtraInterfaces(anotherClass.getClass()));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testValidationMockedTypeWithDifferentClassButImplementsDifferentInterface() {
 Object anotherClass = new Object() {
 @Override
 public void method1() {}
 @Override
 public void method2() {}
 };
 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setSpiedInstance(anotherClass).setExtraInterfaces(new Class[]{anotherClass.getClass()}));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testValidationMockedTypeWithAncillaryTypes() {
 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setAncillaryTypes(new Class[0]));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setAncillaryTypes(new Class[]{String.class, Integer.class}));
 } catch (IllegalArgumentException e) {
 fail("Should not have thrown exception");
 }
}

 @Test
 public void testValidationMockedTypeWithNullAncillaryTypes() {
 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setAncillaryTypes(null));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testValidationMockedTypeWithSameAncillaryType() {
 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setAncillaryTypes(String.class.getClass()));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testValidationMockedTypeWithDifferentAncillaryTypes() {
 try {
 mockUtil.createMock(Object.class, new MockSettingsImpl().setAncillaryTypes(new Class[]{String.class, Integer.class}));
 } catch (IllegalArgumentException e) {
 fail("Should not have thrown exception");
 }
}

 @Test
 public void testCreationWithDefaultAnswer() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().withSettings(new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS)));
 Mockito.verify(mock, Mockito.times(1)).answer(ArgumentMatchers.any());
 }

 @Test
 public void testCreationWithCustomAnswer() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().withSettings(new MockSettingsImpl().answer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "custom answer";
 }
 })));
 assertEquals("custom answer", mock.answer(ArgumentMatchers.any()));
 }

 @Test
 public void testCreationWithCustomAnswer2() {
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl().withSettings(new MockSettingsImpl().answer(new Answer2<Object, Integer>() {
 @Override
 public Object answer(Object invocation, Integer i) throws Throwable {
 return i * 2;
 }
 })));
 assertEquals(2, mock.answer(2));
 }
}