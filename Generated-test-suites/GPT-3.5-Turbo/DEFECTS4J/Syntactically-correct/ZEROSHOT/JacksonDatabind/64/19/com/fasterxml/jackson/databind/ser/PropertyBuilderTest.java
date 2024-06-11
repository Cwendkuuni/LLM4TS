import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setUp() {
        config = mock(SerializationConfig.class);
        beanDesc = mock(BeanDescription.class);
        annotationIntrospector = mock(AnnotationIntrospector.class);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        when(config.getAnnotationIntrospector()).thenReturn(annotationIntrospector);
    }

    @Test
    public void getClassAnnotations() {
        Annotations annotations = mock(Annotations.class);
        when(beanDesc.getClassAnnotations()).thenReturn(annotations);

        Annotations result = propertyBuilder.getClassAnnotations();

        assertSame(annotations, result);
    }
}