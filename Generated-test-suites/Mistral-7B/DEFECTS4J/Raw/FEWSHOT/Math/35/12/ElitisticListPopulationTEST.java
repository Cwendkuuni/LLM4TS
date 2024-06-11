package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;

/**
 * Test class for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    @SuppressWarnings("unchecked")
    private static <T extends Chromosome> ElitisticListPopulation<T> createPopulation(int size, double elitismRate) {
        List<T> initialPopulation = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            initialPopulation.add((T) new DummyChromosome(i));
        }
        return new ElitisticListPopulation<>(initialPopulation, 10, elitismRate);
    }

    @Test
    public void testCreateElitisticListPopulation() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(5, 0.5);
        assertEquals(5, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testCreateElitisticListPopulationWithInvalidElitismRate() {
        createPopulation(5, 1.5);
    }

    @Test
    public void testNextGenerationEmptyInitialPopulation() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(0, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertTrue(nextGeneration.size() > 0);
    }

    @Test
    public void testNextGenerationNullInitialPopulation() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(0, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertTrue(nextGeneration.size() > 0);

        population = new ElitisticListPopulation<>(null, 10, 0.5);
        nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertTrue(nextGeneration.size() > 0);
    }

    @Test
    public void testSortChromosomes() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(10, 0.5);
        Collections.sort(population);
        DummyChromosome bestChromosome = population.get(0);
        DummyChromosome worstChromosome = population.get(population.size() - 1);

        assertNotNull(bestChromosome);
        assertNotNull(worstChromosome);
        assertTrue(bestChromosome.compareTo(worstChromosome) > 0);
    }

    @Test
    public void testNextGenerationEmptyElitism() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(1, 1.0);
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.size());
        assertEquals(population.get(0), nextGeneration.get(0));
    }

    @Test
    public void testNextGenerationNonEmptyElitism() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(5, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        assertTrue(nextGeneration.subList(0, 2).containsAll(population.subList(0, 2)));
        assertTrue(nextGeneration.subList(2, nextGeneration.size()).isEmpty());
    }

    @Test
    public void testNextGenerationWithNewChromosome() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(5, 0.5);
        Chromosome newChromosome = new DummyChromosome(11);

        Population nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.size());
        assertFalse(nextGeneration.contains(newChromosome));

        population.addChromosome(newChromosome);
        nextGeneration = population.nextGeneration();
        assertEquals(6, nextGeneration.size());
        assertTrue(nextGeneration.contains(newChromosome));
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(5, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateBelowZero() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(5, 0.5);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateAboveOne() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(5, 0.5);
        population.setElitismRate(1.1);
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation<DummyChromosome> population = createPopulation(5, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.001);
    }

}

class DummyChromosome implements Chromosome {

    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Chromosome o) {
        DummyChromosome other = (DummyChromosome) o;
        return FastMath.compareTo(id, other.id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}