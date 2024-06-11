import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.*;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(SomeAnnotation.class));

        SomeAnnotation annotation = new SomeAnnotationImpl();
        annotationMap.add(annotation);

        assertSame(annotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        SomeAnnotation annotation = new SomeAnnotationImpl();
        annotationMap.add(annotation);

        List<Annotation> expectedAnnotations = Collections.singletonList(annotation);
        assertEquals(expectedAnnotations, annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        SomeAnnotation annotation1 = new SomeAnnotationImpl();
        primary.add(annotation1);

        SomeAnnotation annotation2 = new SomeAnnotationImpl();
        secondary.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertSame(annotation1, merged.get(SomeAnnotation.class));
        assertSame(annotation2, merged.get(SomeAnnotation.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        SomeAnnotation annotation = new SomeAnnotationImpl();
        annotationMap.add(annotation);

        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        SomeAnnotation annotation = new SomeAnnotationImpl();

        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        SomeAnnotation annotation = new SomeAnnotationImpl();
        annotationMap.add(annotation);

        assertEquals("{@SomeAnnotation=SomeValue}", annotationMap.toString());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        SomeAnnotation annotation = new SomeAnnotationImpl();

        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }
}

class SomeAnnotationImpl implements SomeAnnotation {
    @Override
    public Class<? extends Annotation> annotationType() {
        return SomeAnnotation.class;
    }
}

@interface SomeAnnotation {
    String value() default "SomeValue";
}