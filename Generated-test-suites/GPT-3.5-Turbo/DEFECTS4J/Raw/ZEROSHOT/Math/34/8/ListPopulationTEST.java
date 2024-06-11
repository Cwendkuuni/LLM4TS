import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private ListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<Chromosome>();
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(5);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        Chromosome newChromosome1 = new Chromosome();
        newChromosome1.setFitness(8);
        Chromosome newChromosome2 = new Chromosome();
        newChromosome2.setFitness(12);
        newChromosomes.add(newChromosome1);
        newChromosomes.add(newChromosome2);

        population.setChromosomes(newChromosomes);

        assertEquals(newChromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        Chromosome newChromosome1 = new Chromosome();
        newChromosome1.setFitness(8);
        Chromosome newChromosome2 = new Chromosome();
        newChromosome2.setFitness(12);
        newChromosomes.add(newChromosome1);
        newChromosomes.add(newChromosome2);

        population.addChromosomes(newChromosomes);

        List<Chromosome> expectedChromosomes = new ArrayList<Chromosome>();
        expectedChromosomes.addAll(chromosomes);
        expectedChromosomes.addAll(newChromosomes);

        assertEquals(expectedChromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosome() {
        Chromosome newChromosome = new Chromosome();
        newChromosome.setFitness(8);

        population.addChromosome(newChromosome);

        List<Chromosome> expectedChromosomes = new ArrayList<Chromosome>();
        expectedChromosomes.addAll(chromosomes);
        expectedChromosomes.add(newChromosome);

        assertEquals(expectedChromosomes, population.getChromosomes());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = population.getFittestChromosome();

        assertEquals(chromosomes.get(0), fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = population.getPopulationLimit();

        assertEquals(10, populationLimit);
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(20);

        int populationLimit = population.getPopulationLimit();

        assertEquals(20, populationLimit);
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = population.getPopulationSize();

        assertEquals(2, populationSize);
    }

    @Test
    public void testToString() {
        String populationString = population.toString();

        assertEquals("[Chromosome@1, Chromosome@1]", populationString);
    }
}

class Chromosome {
    private int fitness;
    
    public int getFitness() {
        return fitness;
    }
    
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}