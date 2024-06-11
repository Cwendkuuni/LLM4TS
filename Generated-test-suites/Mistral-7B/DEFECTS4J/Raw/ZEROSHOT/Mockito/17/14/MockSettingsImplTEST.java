import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assert(mockSettings.isSerializable());
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 mockSettings = mockSettings.extraInterfaces(new Object[] {});
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 Class<?>[] extraInterfaces = new Class[] {Integer.TYPE};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[] {Serializable.class, Integer.TYPE};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assert(Arrays.asList(mockSettings.getExtraInterfaces()).contains(Serializable.class));
 assert(Arrays.asList(mockSettings.getExtraInterfaces()).contains(Integer.TYPE.getClass()));
 }

 @Test
 public void testGetMockName() {
 mockSettings.initiateMockName(String.class);
 assertNotNull(mockSettings.getMockName());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 String name = "testName";
 mockSettings = mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<String> answer = Mockito.answer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 });
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }
}