
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: test get() when _annotations is null
        assert(annotationMap.get(Override.class) == null);

        // Test case 2: test get() when _annotations is not null but does not contain the requested annotation
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        annotationMap = new AnnotationMap(annotations);
        assert(annotationMap.get(Deprecated.class) == null);

        // Test case 3: test get() when _annotations contains the requested annotation
        assert(annotationMap.get(Override.class) instanceof OverrideImpl);
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: test annotations() when _annotations is null or empty
        Iterable<Annotation> emptyIterable = Collections.emptyList();
        assert(annotationMap.annotations().equals(emptyIterable));

        // Test case 2: test annotations() when _annotations is not null or not empty
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        annotationMap = new AnnotationMap(annotations);
        List<Annotation> expectedList = new ArrayList<Annotation>();
        expectedList.add(new OverrideImpl());
        assert(annotationMap.annotations().equals(expectedList));
    }

    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();

        // Test case 1: test merge() when primary and secondary both are null or empty
        assert(AnnotationMap.merge(primaryMap, secondaryMap).size() == 0);

        // Test case 2: test merge() when primary is null or empty and secondary is not null or not empty
        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
        secondaryAnnotations.put(Deprecated.class, new DeprecatedImpl());
        secondaryMap = new AnnotationMap(secondaryAnnotations);
        assert(AnnotationMap.merge(primaryMap, secondaryMap).size() == 1);
        assert(AnnotationMap.merge(primaryMap, secondaryMap).get(Deprecated.class) instanceof DeprecatedImpl);

        // Test case 3: test merge() when primary is not null or not empty and secondary is null or empty
        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
        primaryAnnotations.put(Override.class, new OverrideImpl());
        primaryMap = new AnnotationMap(primaryAnnotations);
        assert(AnnotationMap.merge(primaryMap, secondaryMap).size() == 1);
        assert(AnnotationMap.merge(primaryMap, secondaryMap).get(Override.class) instanceof OverrideImpl);

        // Test case 4: test merge() when both primary and secondary are not null or not empty
        assert(AnnotationMap.merge(primaryMap, secondaryMap).size() == 2);

        // Test case 5: test merge() when primary and secondary have common annotations
        assert(AnnotationMap.merge(primaryMap, secondaryMap).get(Override.class) instanceof OverrideImpl);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: test size() when _annotations is null
        assert(annotationMap.size() == 0);

        // Test case 2: test size() when _annotations is not null
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        annotationMap = new AnnotationMap(annotations);
        assert(annotationMap.size() == 1);
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: test addIfNotPresent() when _annotations is null or does not contain the annotation
        assert(annotationMap.addIfNotPresent(new OverrideImpl()));
        assert(annotationMap.addIfNotPresent(new DeprecatedImpl()));

        // Test case 2: test addIfNotPresent() when _annotations contains the annotation
        assert(!annotationMap.addIfNotPresent(new OverrideImpl()));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: test add() when _annotations is null or does not contain the annotation
        assert(annotationMap.add(new OverrideImpl()));
        assert(annotationMap.add(new DeprecatedImpl()));

        // Test case 2: test add() when _annotations contains the annotation
        assert(annotationMap.add(new OverrideImpl()));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: test toString() when _annotations is null
        assert(annotationMap.toString().equals("[null]"));

        // Test case 2: test toString() when _annotations is not null
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        annotationMap = new AnnotationMap(annotations);
        assert(annotationMap.toString().equals(annotations.toString()));
    }

    private class OverrideImpl implements Override {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Override.class;
        }
    }

    private class DeprecatedImpl implements Deprecated {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Deprecated.class;
        }
    }
}
