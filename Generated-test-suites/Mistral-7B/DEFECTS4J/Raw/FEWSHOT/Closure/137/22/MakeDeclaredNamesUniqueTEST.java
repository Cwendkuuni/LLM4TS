package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testContextualRenamer() {
        Renamer renamer = new ContextualRenamer();

        String originalName = "a";
        String renamedName = renamer.getReplacementName(originalName);
        assertEquals(originalName, renamedName);

        renamer.addDeclaredName(originalName);
        renamedName = renamer.getReplacementName(originalName);
        assertTrue(renamedName.contains(ContextualRenamer.UNIQUE_ID_SEPARATOR));

        renamer.addDeclaredName("b");
        renamedName = renamer.getReplacementName(originalName);
        assertEquals("a" + ContextualRenamer.UNIQUE_ID_SEPARATOR + "1", renamedName);

        renamer.addDeclaredName("a");
        renamedName = renamer.getReplacementName(originalName);
        assertEquals("a" + ContextualRenamer.UNIQUE_ID_SEPARATOR + "2", renamedName);
    }

    @Test
    public void testInlineRenamer() {
        String prefix = "_renamed_";
        Renamer renamer = new InlineRenamer(
                () -> prefix + Long.toString(System.nanoTime()), prefix, true);

        String originalName = "a";
        String renamedName = renamer.getReplacementName(originalName);
        assertTrue(renamedName.startsWith(prefix));

        renamer.addDeclaredName(originalName);
        renamedName = renamer.getReplacementName(originalName);
        assertTrue(renamedName.contains(ContextualRenamer.UNIQUE_ID_SEPARATOR));

        renamer.addDeclaredName("b");
        renamedName = renamer.getReplacementName(originalName);
        assertEquals("a" + ContextualRenamer.UNIQUE_ID_SEPARATOR + prefix + "1", renamedName);

        renamer.addDeclaredName("a");
        renamedName = renamer.getReplacementName(originalName);
        assertEquals("a" + ContextualRenamer.UNIQUE_ID_SEPARATOR + prefix + "2", renamedName);
    }

    @Test
    public void testGetOriginalName() {
        String nameWithId = "a$$1";
        String originalName = MakeDeclaredNamesUnique.getOrginalName(nameWithId);
        assertEquals("a", originalName);

        originalName = MakeDeclaredNamesUnique.getOrginalName("a");
        assertEquals("a", originalName);
    }

    @Test
    public void testContainsSeparator() {
        assertTrue(MakeDeclaredNamesUnique.containsSeparator("a$$1"));
        assertFalse(MakeDeclaredNamesUnique.containsSeparator("a"));
    }

    @Test
    public void testGetNameSuffix() {
        String nameWithId = "a$$1";
        String suffix = MakeDeclaredNamesUnique.getNameSuffix(nameWithId, 0);
        assertEquals("", suffix);

        suffix = MakeDeclaredNamesUnique.getNameSuffix("a$$1", 1);
        assertEquals("1", suffix);
    }

    @Test
    public void testGetNameSuffixWithEmptyName() {
        String suffix = MakeDeclaredNamesUnique.getNameSuffix("", 0);
        assertEquals("", suffix);
    }

    @Test
    public void testGetNameSuffixWithIdOnlyName() {
        String suffix = MakeDeclaredNamesUnique.getNameSuffix("$$1", 0);
        assertEquals("", suffix);
    }
}

class ContextualRenamer implements MakeDeclaredNamesUnique.Renamer {
    static final String UNIQUE_ID_SEPARATOR = "$$";

    private Multiset<String> nameUsage;
    private Map<String, String> declarations = new HashMap<>();
    private boolean global;

    ContextualRenamer() {
        this.global = true;
        this.nameUsage = HashMultiset.create();
    }

    ContextualRenamer(Multiset<String> nameUsage) {
        this.global = false;
        this.nameUsage = nameUsage;
    }

    @Override
    public void addDeclaredName(String name) {
        if (global) {
            reserveName(name);
        } else {
            int id = incrementNameCount(name);
            String newName = getUniqueName(name, id);
            declarations.put(name, newName);
        }
    }

    @Override
    public String getReplacementName(String oldName) {
        return declarations.getOrDefault(oldName, oldName);
    }

    @Override
    public boolean stripConstIfReplaced() {
        return false;
    }

    @Override
    public Renamer forChildScope() {
        return new ContextualRenamer(nameUsage);
    }

    private void reserveName(String name) {
        nameUsage.setCount(name, 0, 1);
    }

    private int incrementNameCount(String name) {
        return nameUsage.add(name, 1);
    }

    private String getUniqueName(String name, int id) {
        return name + UNIQUE_ID_SEPARATOR + id;
    }
}

class InlineRenamer implements MakeDeclaredNamesUnique.Renamer {
    private final Supplier<String> uniqueIdSupplier;
    private final String idPrefix;
    private final boolean removeConstness;

    InlineRenamer(Supplier<String> uniqueIdSupplier, String idPrefix, boolean removeConstness) {
        this.uniqueIdSupplier = uniqueIdSupplier;
        this.idPrefix = idPrefix;
        this.removeConstness = removeConstness;
    }

    @Override
    public void addDeclaredName(String name) {
        if (!declarations.containsKey(name)) {
            declarations.put(name, getUniqueName(name));
        }
    }

    @Override
    public String getReplacementName(String oldName) {
        return declarations.getOrDefault(oldName, oldName);
    }

    @Override
    public Renamer forChildScope() {
        return new InlineRenamer(uniqueIdSupplier, idPrefix, removeConstness);
    }

    @Override
    public boolean stripConstIfReplaced() {
        return removeConstness;
    }
}