import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        Retention retention = AnnotationMapTest.class.getAnnotation(Retention.class);
        annotationMap.add(retention);

        assertEquals(retention, annotationMap.get(Retention.class));
    }

    @Test
    public void testAnnotations() {
        List<Annotation> expected = new ArrayList<>();
        expected.add(AnnotationMapTest.class.getAnnotation(Retention.class));
        annotationMap.add(expected.get(0));

        Iterable<Annotation> actual = annotationMap.annotations();
        List<Annotation> actualList = new ArrayList<>();
        actual.forEach(actualList::add);

        assertEquals(expected, actualList);
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        Retention retention1 = AnnotationMapTest.class.getAnnotation(Retention.class);
        Retention retention2 = AnnotationMapTest.class.getAnnotation(Retention.class);

        primary.add(retention1);
        secondary.add(retention2);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(retention1, result.get(Retention.class));
    }

    @Test
    public void testSize() {
        Retention retention = AnnotationMapTest.class.getAnnotation(Retention.class);
        annotationMap.add(retention);

        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        Retention retention = AnnotationMapTest.class.getAnnotation(Retention.class);

        assertTrue(annotationMap.addIfNotPresent(retention));
        assertFalse(annotationMap.addIfNotPresent(retention));
    }

    @Test
    public void testToString() {
        Retention retention = AnnotationMapTest.class.getAnnotation(Retention.class);
        annotationMap.add(retention);

        String expected = "{interface java.lang.annotation.Retention=" + retention.toString() + "}";
        assertEquals(expected, annotationMap.toString());
    }
}