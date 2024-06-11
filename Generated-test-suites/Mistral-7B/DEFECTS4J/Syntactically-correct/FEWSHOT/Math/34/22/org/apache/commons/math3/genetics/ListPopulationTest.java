package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithEmptyList() {
        ListPopulation pop = new ListPopulation(5);
        assertEquals(0, pop.getPopulationSize());
        assertEquals(5, pop.getPopulationLimit());
        assertNull(pop.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationWithPopulatedList() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation pop = new ListPopulation(initialChromosomes, 10);
        assertEquals(initialChromosomes.size(), pop.getPopulationSize());
        assertEquals(10, pop.getPopulationLimit());
        assertEquals(initialChromosomes, pop.getChromosomes());
    }

    @Test
    public void testListPopulationSetChromosomesNullArgumentException() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 10);
        assertThrows(NullArgumentException.class, () -> pop.setChromosomes(null));
    }

    @Test
    public void testListPopulationSetChromosomesNumberIsTooLargeException() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation pop = new ListPopulation(initialChromosomes, 5);
        assertThrows(NumberIsTooLargeException.class, () -> pop.setChromosomes(Arrays.asList(new Chromosome() {})));
    }

    @Test
    public void testListPopulationAddChromosomesNullArgumentException() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 10);
        assertThrows(NullArgumentException.class, () -> pop.addChromosomes(null));
    }

    @Test
    public void testListPopulationAddChromosomesNumberIsTooLargeException() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        assertThrows(NumberIsTooLargeException.class, () -> pop.addChromosomes(Arrays.asList(new Chromosome() {}, new Chromosome() {}, new Chromosome() {})));
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new Chromosome() {};
        pop.addChromosome(chromosome);
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosome, pop.getChromosomes().get(0));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {}, new Chromosome() {});
        ListPopulation pop = new ListPopulation(initialChromosomes, 5);
        Chromosome expectedFittestChromosome = pop.getFittestChromosome();
        for (Chromosome chromosome : pop.getChromosomes()) {
            if (chromosome != expectedFittestChromosome && chromosome.compareTo(expectedFittestChromosome) > 0) {
                pop.setChromosomes(Collections.singletonList(expectedFittestChromosome));
                break;
            }
        }
        assertEquals(expectedFittestChromosome, pop.getFittestChromosome());
    }

    @Test
    public void testListPopulationSetPopulationLimitNotPositiveException() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        assertThrows(NotPositiveException.class, () -> pop.setPopulationLimit(-1));
    }

    @Test
    public void testListPopulationSetPopulationLimitNumberIsTooSmallException() {
        ListPopulation pop = new ListPopulation(Arrays.asList(new Chromosome() {}, new Chromosome() {}), 5);
        assertThrows(NumberIsTooSmallException.class, () -> pop.setPopulationLimit(4));
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(5, pop.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.setPopulationLimit(10);
        assertEquals(10, pop.getPopulationLimit());
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(0, pop.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation pop = new ListPopulation(initialChromosomes, 10);
        String expectedString = "[ListPopulation: populationSize=2, populationLimit=10, chromosomes=[], toString=]";
        assertEquals(expectedString, pop.toString());
    }

    @Test
    public void testListPopulationIterator() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {}, new Chromosome() {});
        ListPopulation pop = new ListPopulation(initialChromosomes, 10);
        Iterator<Chromosome> iterator = pop.iterator();
        Iterator<Chromosome> expectedIterator = initialChromosomes.iterator();
        assertSame(iterator, expectedIterator);
        assertTrue(iterator.hasNext());
        assertSame(iterator.next(), expectedIterator.next());
        assertFalse(iterator.hasNext());
    }

}