package com.google.javascript.jscomp;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static com.google.javascript.jscomp.RenamePrototypes.FREQUENCY_COMPARATOR;

@RunWith(MockitoJUnitRunner.class)
public class RenamePrototypesTest {

    @Mock
    private AbstractCompiler compiler;
    @Mock
    private VariableMap prevUsedRenameMap;

    private RenamePrototypes renamePrototypes;

    @Before
    public void setUp() {
        renamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);
    }

    @Test
    public void testCanRenamePrototypeProperty() {
        String testProperty = "_testProperty";
        Property property = new Property(testProperty);

        assertFalse(property.canRename());
        when(compiler.getCodingConvention().isExported(testProperty)).thenReturn(false);
        when(compiler.getCodingConvention().isPrivate(testProperty)).thenReturn(false);
        when(compiler.getCodingConvention().isPrivate(testProperty)).thenReturn(true);
        assertTrue(property.canRename());
    }

    @Test
    public void testCanRenameObjLitProperty() {
        String testProperty = "_testProperty";
        Property property = new Property(testProperty);

        assertFalse(property.canRenameObjLitProperty());
        when(compiler.getCodingConvention().isExported(testProperty)).thenReturn(false);
        when(compiler.getCodingConvention().isPrivate(testProperty)).thenReturn(false);
        when(compiler.getCodingConvention().isPrivate(testProperty)).thenReturn(true);
        when(compiler.getCodingConvention().isExported(testProperty)).thenReturn(true);
        assertFalse(property.canRenameObjLitProperty());
        when(compiler.getCodingConvention().isExported(testProperty)).thenReturn(false);
        when(compiler.getCodingConvention().isPrivate(testProperty)).thenReturn(false);
        when(compiler.getCodingConvention().isExported("testProperty")).thenReturn(true);
        when(compiler.getCodingConvention().isPrivate("testProperty")).thenReturn(false);
        assertFalse(property.canRenameObjLitProperty());
        when(compiler.getCodingConvention().isExported("testProperty")).thenReturn(false);
        when(compiler.getCodingConvention().isPrivate("testProperty")).thenReturn(true);
        assertTrue(property.canRenameObjLitProperty());
    }

    @Test
    public void testFrequencyComparator() {
        Property p1 = new Property("test1");
        Property p2 = new Property("test2");
        Property p3 = new Property("test3");

        Set<Property> properties = new TreeSet<>(FREQUENCY_COMPARATOR);

        properties.add(p1);
        properties.add(p2);
        properties.add(p3);

        Property expected = p3;
        Property actual = properties.first();

        assertEquals(expected.oldName, actual.oldName);
    }
}