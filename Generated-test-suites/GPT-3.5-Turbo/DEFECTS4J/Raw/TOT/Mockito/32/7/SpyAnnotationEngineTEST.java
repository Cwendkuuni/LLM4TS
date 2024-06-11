import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Spy
    private List<String> spyList;

    @Spy
    private List<String> nullList;

    @Mock
    private List<String> mockList;

    @Captor
    private ArgumentCaptor<List<String>> captor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = spyList.getClass().getDeclaredAnnotations()[0];
        Field field = null;
        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        Assert.assertNull(result);
    }

    @Test
    public void testProcess_withSpyField() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = this;

        spyAnnotationEngine.process(context, testClass);

        Assert.assertTrue(spyList instanceof List);
        Assert.assertTrue(spyList instanceof LinkedList);
        Assert.assertEquals(0, spyList.size());
    }

    @Test(expected = MockitoException.class)
    public void testProcess_withSpyFieldInstanceMissing() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = this;

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_withSpyFieldInstanceReset() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = this;

        spyList.add("item1");

        spyAnnotationEngine.process(context, testClass);

        Assert.assertEquals(0, spyList.size());
    }

    @Test
    public void testProcess_withSpyFieldFailed() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = this;

        try {
            nullList.add("item1");

            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            Assert.assertTrue(e.getMessage().contains("Problems initiating spied field nullList"));
        }
    }

    @Test(expected = Reporter.class)
    public void testAssertNoAnnotations_withUndesiredAnnotation() throws Exception {
        Field field = SpyAnnotationEngine.class.getDeclaredField("spyList");
        Class<Spy> annotation = Spy.class;
        Class<?> undesiredAnnotation = Mock.class;

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation);
    }

    @Test
    public void testAssertNoAnnotations_withoutUndesiredAnnotation() throws Exception {
        Field field = SpyAnnotationEngine.class.getDeclaredField("spyList");
        Class<Spy> annotation = Spy.class;
        Class<?> undesiredAnnotation = Captor.class;

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation);
    }

}