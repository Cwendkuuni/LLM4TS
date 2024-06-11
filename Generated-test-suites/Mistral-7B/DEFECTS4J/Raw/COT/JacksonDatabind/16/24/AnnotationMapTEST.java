import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap map = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        map.add(annotation);

        Assert.assertEquals(annotation, map.get(MyAnnotation.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullAnnotation() {
        AnnotationMap map = new AnnotationMap();
        Assert.assertNull(map.get(null));
    }

    @Test
    public void testGetEmptyAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Assert.assertTrue(map.annotations().isEmpty());
    }

    @Test
    public void testMergeEmptyMaps() {
        AnnotationMap map1 = new AnnotationMap();
        AnnotationMap map2 = new AnnotationMap();

        AnnotationMap mergedMap = AnnotationMap.merge(map1, map2);
        Assert.assertSame(map1, mergedMap);
    }

    @Test
    public void testMergeNonEmptyMaps() {
        AnnotationMap map1 = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        map1.add(ann1);

        AnnotationMap map2 = new AnnotationMap();
        MyAnnotation ann2 = new MyAnnotation();
        map2.add(ann2);

        AnnotationMap mergedMap = AnnotationMap.merge(map1, map2);
        Assert.assertNotSame(map1, mergedMap);
        Assert.assertNotSame(map2, mergedMap);
        Assert.assertEquals(2, mergedMap.size());
        Assert.assertArrayEquals(new Object[]{ann1, ann2}, mergedMap.annotations().toArray());
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        map.add(new MyAnnotation());
        map.add(new MyAnnotation());

        Assert.assertEquals(2, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();

        Assert.assertTrue(map.addIfNotPresent(annotation));
        Assert.assertEquals(annotation, map.get(MyAnnotation.class));

        Annotation otherAnnotation = new MyAnnotation();
        Assert.assertTrue(map.addIfNotPresent(otherAnnotation));
        Assert.assertNotSame(annotation, otherAnnotation);
        Assert.assertEquals(2, map.size());

        Assert.assertFalse(map.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();

        Assert.assertTrue(map.add(annotation));
        Assert.assertEquals(annotation, map.get(MyAnnotation.class));

        Annotation otherAnnotation = new MyAnnotation();
        Assert.assertTrue(map.add(otherAnnotation));
        Assert.assertNotSame(annotation, otherAnnotation);
        Assert.assertEquals(2, map.size());

        Assert.assertFalse(map.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        map.add(new MyAnnotation());
        map.add(new MyAnnotation());

        Assert.assertEquals("[Lcom.example.MyAnnotation;@15db9742]", map.toString());
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        secondary.add(ann1);

        AnnotationMap mergedMap = AnnotationMap.merge(null, secondary);
        Assert.assertSame(secondary, mergedMap);
    }

    @Test
    public void testMergeNullSecondary() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        primary.add(ann1);

        AnnotationMap mergedMap = AnnotationMap.merge(primary, null);
        Assert.assertSame(primary, mergedMap);
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNullAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        Assert.assertNotSame(primary, mergedMap);
        Assert.assertNotSame(secondary, mergedMap);
        Assert.assertNull(mergedMap.annotations());
    }

    @Test
    public void testMergeDifferentAnnotationTypes() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        primary.add(ann1);

        AnnotationMap secondary = new AnnotationMap();
        AnotherAnnotation ann2 = new AnotherAnnotation();
        secondary.add(ann2);

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        Assert.assertNotSame(primary, mergedMap);
        Assert.assertNotSame(secondary, mergedMap);
        Assert.assertEquals(2, mergedMap.size());
        Assert.assertArrayEquals(new Object[]{ann1, ann2}, mergedMap.annotations().toArray());
    }

    @Test
    public void testGetEmptyAnnotationArray() {
        AnnotationMap map = new AnnotationMap();
        Assert.assertArrayEquals(new Object[0], map.annotations().toArray());
    }

    @Test
    public void testGetAnnotationArrayWithAnnotations() {
        AnnotationMap map = new AnnotationMap();
        map.add(new MyAnnotation());
        map.add(new MyAnnotation());

        Assert.assertArrayEquals(new Object[]{new MyAnnotation(), new MyAnnotation()}, map.annotations().toArray());
    }

    @Test
    public void testGetAnnotationByTypeWithEmptyMap() {
        AnnotationMap map = new AnnotationMap();
        Assert.assertNull(map.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotationByTypeWithSingleAnnotation() {
        AnnotationMap map = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        map.add(annotation);

        Assert.assertEquals(annotation, map.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotationByTypeWithMultipleAnnotations() {
        AnnotationMap map = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        MyAnnotation ann2 = new MyAnnotation();
        map.add(ann1);
        map.add(ann2);

        Assert.assertArrayEquals(new Object[]{ann1, ann2}, map.annotations().toArray());
        Assert.assertEquals(ann1, map.get(MyAnnotation.class));
        Assert.assertEquals(ann2, map.get(MyAnnotation.class));
    }
}