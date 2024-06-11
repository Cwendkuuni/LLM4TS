package com.google.javascript.jscomp;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {
    
    // Renamer for testing purposes
    private static class TestRenamer implements MakeDeclaredNamesUnique.Renamer {
        private final ImmutableSet<String> declaredNames;
        
        public TestRenamer(String[] declaredNames) {
            this.declaredNames = ImmutableSet.copyOf(declaredNames);
        }
        
        @Override
        public void addDeclaredName(String name) {
            // do nothing
        }
        
        @Override
        public String getReplacementName(String oldName) {
            if (declaredNames.contains(oldName)) {
                return oldName + "_renamed";
            }
            return null;
        }
        
        @Override
        public Renamer forChildScope() {
            return this;
        }
        
        @Override
        public boolean stripConstIfReplaced() {
            return false;
        }
    }
    
    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.Renamer renamer = new TestRenamer(new String[] {"foo", "bar"});
        
        assertEquals("foo_renamed", renamer.getReplacementName("foo"));
        assertEquals("bar_renamed", renamer.getReplacementName("bar"));
        assertNull(renamer.getReplacementName("baz"));
    }
}