
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(10);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes().subList(0, 2));
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        assertEquals(0, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(2);
        assertEquals(2, population.getPopulationSize());
        assertEquals(2, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        population.setPopulationLimit(-1);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }
        assertEquals(chromosomes.size(), count);
    }
}

class Chromosome implements Comparable<Chromosome> {
    private int fitness;

    public Chromosome() {
        this.fitness = 0;
    }

    public Chromosome(int fitness) {
        this.fitness = fitness;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Chromosome other) {
        return Integer.compare(this.fitness, other.fitness);
    }
}
