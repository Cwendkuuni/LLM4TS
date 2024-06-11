package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

 private MockSettings settings;

 @Before
 public void setUp() {
 settings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 settings = settings.serializable();
 assertTrue(settings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 settings = settings.extraInterfaces(new Object[] {});
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesDoesNotAcceptNullParameters() {
 settings = settings.extraInterfaces(new Class[] { null });
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 settings = settings.extraInterfaces(new Class[] { String.class });
 }

 @Test
 public void testExtraInterfaces() {
 settings = settings.extraInterfaces(new Class[] { java.io.Serializable.class, java.util.List.class });
 assertArrayEquals(new Class[] { java.io.Serializable.class, java.util.List.class }, settings.getExtraInterfaces());
 }

 @Test
 public void testGetMockName() {
 assertNull(settings.getMockName());
 settings = settings.name("testMockName");
 assertNotNull(settings.getMockName());
 assertEquals("testMockName", settings.getMockName().getName());
 }

 @Test
 public void testGetSpiedInstance() {
 assertNull(settings.getSpiedInstance());
 settings.spiedInstance(new Object());
 assertSame(new Object(), settings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = org.mockito.internal.creation.answers.ConstantsAnswer.RETURN_CONSTANT_WITH_VALUE_1;
 settings = settings.defaultAnswer(answer);
 assertSame(answer, settings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 settings = settings.serializable();
 assertTrue(settings.isSerializable());
 settings = settings.extraInterfaces(new Class[] { java.io.Serializable.class });
 assertTrue(settings.isSerializable());
 settings = settings.extraInterfaces(new Class[] {});
 assertFalse(settings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class classToMock = MockitoTestUtils.class;
 settings = settings.name("testMockName").spiedInstance(new Object()).extraInterfaces(new Class[] { java.io.Serializable.class });
 settings.initiateMockName(classToMock);
 assertNotNull(settings.getMockName());
 assertEquals("testMockName", settings.getMockName().getName());
 assertEquals(classToMock, settings.getMockName().getMockType());
 }
}