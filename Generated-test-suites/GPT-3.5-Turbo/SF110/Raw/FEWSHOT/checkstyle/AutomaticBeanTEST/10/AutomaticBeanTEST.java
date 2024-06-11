package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean.RelaxedStringArrayConverter;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.util.*;

public class AutomaticBeanTest {
	
	private AutomaticBean automaticBean;
	private ConvertUtilsBean convertUtilsBean;
	private PropertyUtilsBean propertyUtilsBean;

	@Before
	public void setUp() {
		convertUtilsBean = mock(ConvertUtilsBean.class);
		propertyUtilsBean = mock(PropertyUtilsBean.class);
		automaticBean = new AutomaticBean(convertUtilsBean, propertyUtilsBean);
	}

	@Test
	public void testConfigure() throws CheckstyleException {
		Configuration config = mock(Configuration.class);
		PropertyDescriptor descriptor = mock(PropertyDescriptor.class);
		Configuration childConfig = mock(Configuration.class);

		when(config.getAttributeNames()).thenReturn(new String[] {"key1", "key2"});
		when(config.getAttribute("key1")).thenReturn("value1");
		when(config.getAttribute("key2")).thenReturn("value2");
		when(config.getChildren()).thenReturn(new Configuration[] {childConfig});
		when(propertyUtilsBean.getPropertyDescriptor(automaticBean, "key1")).thenReturn(descriptor);
		when(propertyUtilsBean.getPropertyDescriptor(automaticBean, "key2")).thenReturn(null);
		
		automaticBean.configure(config);
		
		verify(config, times(1)).getAttributeNames();
		verify(config, times(1)).getAttribute("key1");
		verify(config, times(1)).getAttribute("key2");
		verify(config, times(1)).getChildren();
		verify(propertyUtilsBean, times(1)).getPropertyDescriptor(automaticBean, "key1");
		verify(propertyUtilsBean, times(1)).getPropertyDescriptor(automaticBean, "key2");
		verify(convertUtilsBean, times(1)).copyProperty(automaticBean, "key1", "value1");
		verify(convertUtilsBean, times(0)).copyProperty(automaticBean, "key2", "value2");
		verify(childConfig, times(1)).getName();
		verifyZeroInteractions(descriptor);
	}

	@Test(expected = CheckstyleException.class)
	public void testConfigure_NonExistentProperty() throws CheckstyleException {
		Configuration config = mock(Configuration.class);
		PropertyDescriptor descriptor = mock(PropertyDescriptor.class);
		Configuration childConfig = mock(Configuration.class);

		when(config.getAttributeNames()).thenReturn(new String[] {"key1", "key2"});
		when(config.getAttribute("key1")).thenReturn("value1");
		when(config.getAttribute("key2")).thenReturn("value2");
		when(config.getChildren()).thenReturn(new Configuration[] {childConfig});
		when(propertyUtilsBean.getPropertyDescriptor(automaticBean, "key1")).thenReturn(descriptor);
		when(propertyUtilsBean.getPropertyDescriptor(automaticBean, "key2")).thenReturn(null);
		
		automaticBean.configure(config);
	}

	@Test(expected = CheckstyleException.class)
	public void testConfigure_SetPropertyException() throws CheckstyleException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Configuration config = mock(Configuration.class);
		PropertyDescriptor descriptor = mock(PropertyDescriptor.class);
		Configuration childConfig = mock(Configuration.class);

		when(config.getAttributeNames()).thenReturn(new String[] {"key"});
		when(config.getAttribute("key")).thenReturn("value");
		when(config.getChildren()).thenReturn(new Configuration[] {childConfig});
		when(propertyUtilsBean.getPropertyDescriptor(automaticBean, "key")).thenReturn(descriptor);
		doThrow(InvocationTargetException.class).when(convertUtilsBean).copyProperty(automaticBean, "key", "value");
		
		automaticBean.configure(config);
	}

	@Test(expected = CheckstyleException.class)
	public void testConfigure_IllegalValueException() throws CheckstyleException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Configuration config = mock(Configuration.class);
		PropertyDescriptor descriptor = mock(PropertyDescriptor.class);
		Configuration childConfig = mock(Configuration.class);

		when(config.getAttributeNames()).thenReturn(new String[] {"key"});
		when(config.getAttribute("key")).thenReturn("value");
		when(config.getChildren()).thenReturn(new Configuration[] {childConfig});
		when(propertyUtilsBean.getPropertyDescriptor(automaticBean, "key")).thenReturn(descriptor);
		doThrow(ConversionException.class).when(convertUtilsBean).copyProperty(automaticBean, "key", "value");
		
		automaticBean.configure(config);
	}

	@Test
	public void testContextualize() throws CheckstyleException {
		Context context = mock(Context.class);

		when(context.getAttributeNames()).thenReturn(new ArrayList<>());
		
		automaticBean.contextualize(context);

		verify(context, times(1)).getAttributeNames();
		verifyZeroInteractions(convertUtilsBean);
	}

	@Test
	public void testContextualize_WithAttribute() throws CheckstyleException {
		Context context = mock(Context.class);

		when(context.getAttributeNames()).thenReturn(Collections.singletonList("key"));
		when(context.get("key")).thenReturn("value");
		
		automaticBean.contextualize(context);

		verify(context, times(1)).getAttributeNames();
		verify(context, times(1)).get("key");
		verify(convertUtilsBean, times(1)).copyProperty(automaticBean, "key", "value");
	}

}