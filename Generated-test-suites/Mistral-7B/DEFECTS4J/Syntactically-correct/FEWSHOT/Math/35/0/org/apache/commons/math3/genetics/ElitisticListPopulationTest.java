package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructionWithListAndLimit() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(0.1));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertEquals(5, population.size());
        assertNotNull(population.getElitismRate());
    }

    @Test
    public void testElitisticListPopulationConstructionWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(5, population.size());
        assertNotNull(population.getElitismRate());
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = Arrays.asList(new DummyChromosome(0.1), new DummyChromosome(0.2),
                new DummyChromosome(0.3), new DummyChromosome(0.4), new DummyChromosome(0.5),
                new DummyChromosome(0.6), new DummyChromosome(0.7), new DummyChromosome(0.8));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        assertEquals(10, population.size());
        List<Chromosome> sortedChromosomes = new ArrayList<>(population);
        Collections.sort(sortedChromosomes, new ChromosomeFitnessComparator());

        Population nextGeneration = population.nextGeneration();
        assertEquals(population.getPopulationLimit(), nextGeneration.size());

        List<Chromosome> elitisticChromosomes = new ArrayList<>();
        for (int i = 0; i < Math.ceil(population.size() * 0.5); i++) {
            elitisticChromosomes.add(sortedChromosomes.get(i));
        }
        List<Chromosome> nextGenerationChromosomes = new ArrayList<>(nextGeneration);
        Collections.sort(nextGenerationChromosomes, new ChromosomeFitnessComparator());

        for (int i = 0; i < elitisticChromosomes.size(); i++) {
            assertEquals(elitisticChromosomes.get(i), nextGenerationChromosomes.get(i));
        }
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        Population nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.size());
        assertNotNull(nextGeneration.getElitismRate());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(1.1);
    }

    @Test
    public void testElitisticListPopulationGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

}

class DummyChromosome implements Chromosome {
    private double fitness;

    public DummyChromosome(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }
}

class ChromosomeFitnessComparator implements Comparator<Chromosome> {
    @Override
    public int compare(Chromosome o1, Chromosome o2) {
        return Double.compare(o1.getFitness(), o2.getFitness());
    }
}