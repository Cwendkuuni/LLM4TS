package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.size());
        assertEquals(5, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationGreaterElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.add(new DummyChromosome(1));
        population.add(new DummyChromosome(2));
        population.add(new DummyChromosome(3));
        population.add(new DummyChromosome(4));
        population.add(new DummyChromosome(5));
        population.add(new DummyChromosome(6));
        population.add(new DummyChromosome(7));
        population.add(new DummyChromosome(8));
        population.add(new DummyChromosome(9));
        population.add(new DummyChromosome(10));

        List<Chromosome> sortedChromosomes = new ArrayList<>(population);
        Collections.sort(sortedChromosomes);

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.size());
        assertEquals(5, nextGeneration.getElitismRate(), 0.01);

        int boundIndex = (int) Math.ceil((1.0 - 0.5) * sortedChromosomes.size());

        for (int i = boundIndex; i < sortedChromosomes.size(); i++) {
            Chromosome chromosome = sortedChromosomes.get(i);
            Chromosome nextGenerationChromosome = nextGeneration.get(i - boundIndex);
            assertSame(chromosome, nextGenerationChromosome);
        }

        assertTrue(nextGeneration.containsAll(sortedChromosomes.subList(boundIndex, sortedChromosomes.size())));
        assertSame(population, nextGeneration.getSourcePopulation());
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateNegative() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateGreater() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(1.1);
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double[] getGenes() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(Chromosome o) {
            return Integer.compare(this.id, ((DummyChromosome) o).id);
        }

        @Override
        public String toString() {
            return String.valueOf(this.id);
        }
    }
}