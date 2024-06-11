import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.MockName;
import org.mockito.stubbing.Answer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<?>[] extraInterfaces;
 private List<Class<?>> invalidInterfaces;
 private Class<?> serializableClass;
 private Object spiedInstance;
 private String name;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 extraInterfaces = new Class[]{Serializable.class, String.class};
 invalidInterfaces = new ArrayList<>();
 serializableClass = Mockito.mock(Serializable.class);
 name = "mockName";
 defaultAnswer = Mockito.answer(new FakeAnswer());
 }

 @Test
 public void testSerializable() {
 mockSettings.serializable();
 assert mockSettings.isSerializable();
 }

 @Test
 public void testExtraInterfaces_Valid() {
 mockSettings.extraInterfaces(extraInterfaces);
 assert mockSettings.getExtraInterfaces().equals(extraInterfaces);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfaces_Null() {
 mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfaces_NonInterface() {
 mockSettings.extraInterfaces(new Object(){});
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfaces_Invalid_NullInterface() {
 mockSettings.extraInterfaces(invalidInterfaces.toArray(new Class[0]));
 invalidInterfaces.add(null);
 mockSettings.extraInterfaces(invalidInterfaces.toArray(new Class[0]));
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfaces_Invalid_NonInterface() {
 mockSettings.extraInterfaces(new Object[]{serializableClass, new Object(){}});
 }

 @Test
 public void testGetName() {
 mockSettings.name(name);
 assert mockSettings.getMockName().getName().equals(name);
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings.spiedInstance(spiedInstance);
 assert mockSettings.getSpiedInstance().equals(spiedInstance);
 }

 @Test
 public void testDefaultAnswer() {
 mockSettings.defaultAnswer(defaultAnswer);
 assert mockSettings.getDefaultAnswer() == defaultAnswer;
 }

 @Test
 public void testIsSerializable() {
 mockSettings.serializable();
 assert mockSettings.isSerializable();
 }

 @Test
 public void testInitiateMockName() {
 mockSettings.initiateMockName(String.class);
 assert mockSettings.getMockName() != null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitiateMockName_NullClass() {
 mockSettings.initiateMockName(null);
 }
}