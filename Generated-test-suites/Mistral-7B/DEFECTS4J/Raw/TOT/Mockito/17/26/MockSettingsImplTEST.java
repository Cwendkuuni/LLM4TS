package org.mockito.internal.creation.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockSettingsImplTest {

 private MockSettings mockSettings;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setUp() {
 mockSettings = mock(MockSettingsImpl.class);
 extraInterfaces = new Class[] {String.class, java.io.Serializable.class};
 name = "MockName";
 spiedInstance = mock(String.class);
 defaultAnswer = Mockito.mock(Answer.class);
 }

 @Test
 public void testSerializable() {
 mockSettings.serializable();
 verify(mockSettings).extraInterfaces(java.io.Serializable.class);
 }

 @Test
 public void testExtraInterfacesNull() {
 try {
 mockSettings.extraInterfaces(null);
 } catch (Reporter reporter) {
 verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
 }
 }

 @Test
 public void testExtraInterfacesNotInterface() {
 try {
 mockSettings.extraInterfaces(Integer.class);
 } catch (Reporter reporter) {
 verify(reporter).extraInterfacesAcceptsOnlyInterfaces(Integer.class);
 }
 }

 @Test
 public void testGetExtraInterfaces() {
 mockSettings.extraInterfaces(extraInterfaces);
 verify(mockSettings).extraInterfaces = extraInterfaces;
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings.spiedInstance(spiedInstance);
 verify(mockSettings).spiedInstance = spiedInstance;
 }

 @Test
 public void testgetName() {
 mockSettings.name(name);
 verify(mockSettings).name = name;
 }

 @Test
 public void testDefaultAnswer() {
 mockSettings.defaultAnswer(defaultAnswer);
 verify(mockSettings).defaultAnswer = defaultAnswer;
 }

 @Test
 public void testIsSerializable() {
 mockSettings.extraInterfaces(new Class[] {java.io.Serializable.class});
 verify(mockSettings).isSerializable();
 }

 @Test
 public void testInitiateMockName() {
 Class classToMock = mock(Class.class);
 mockSettings.initiateMockName(classToMock);
 verify(mockSettings).mockName = new MockName(name, classToMock);
 }
}