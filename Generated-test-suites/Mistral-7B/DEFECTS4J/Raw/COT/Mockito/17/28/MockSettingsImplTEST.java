import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializable() {
 mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 mockSettings = new MockSettingsImpl().extraInterfaces(new Object[0]);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesDoesNotAcceptNullParameters() {
 mockSettings = new MockSettingsImpl().extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 Class<?>[] extraInterfaces = new Class<?>[]{MockSettingsImpl.class};
 mockSettings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(MockSettingsImpl.class);
 }

 @Test
 public void testGetMockName() {
 mockSettings = new MockSettingsImpl();
 String expectedName = "MockName";
 mockSettings.name(expectedName);
 assertEquals(expectedName, mockSettings.getMockName().getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 Class<?>[] expectedExtraInterfaces = new Class<?>[]{java.io.Serializable.class, java.util.List.class};
 mockSettings.extraInterfaces(expectedExtraInterfaces);
 assertArrayEquals(expectedExtraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings = new MockSettingsImpl().spiedInstance(new Object());
 assertEquals(new Object(), mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> defaultAnswer = mock(Answer.class);
 mockSettings = new MockSettingsImpl().defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl().serializable().extraInterfaces(new Class<?>[]{java.io.Serializable.class});
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = MockSettingsImpl.class;
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}