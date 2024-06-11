import org.junit.Test;
import static org.junit.Assert.*;
import geo.google.datamodel.*;
import geo.google.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        // Test case 1: address with no country
        GeoAddress addr = new GeoAddress();
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNull(result.getCountry());
        assertNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        // Test case 2: address with country and state
        addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        country.getCountryNameCode().add(new AddressDetails.Country.CountryNameCode());
        addr.getAddressDetails().setCountry(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().getAdministrativeAreaName().add(new AdministrativeArea.AdministrativeAreaName());
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertNotNull(result.getState());
        assertNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        // Test case 3: address with country, state, and subAdminArea
        addr = new GeoAddress();
        country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertNotNull(result.getState());
        assertNotNull(result.getCounty());
        assertNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        // Test case 4: address with country, state, subAdminArea, and locality
        addr = new GeoAddress();
        country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        country.getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName());
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertNotNull(result.getState());
        assertNotNull(result.getCounty());
        assertNotNull(result.getCity());
        assertNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        // Test case 5: address with country, state, subAdminArea, locality, and postal code
        addr = new GeoAddress();
        country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        country.getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber());
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertNotNull(result.getState());
        assertNotNull(result.getCounty());
        assertNotNull(result.getCity());
        assertNotNull(result.getPostalCode());
        assertNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        // Test case 6: address with country, state, subAdminArea, locality, postal code, and thoroughfare
        addr = new GeoAddress();
        country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        country.getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new Thoroughfare.ThoroughfareNameType());
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertNotNull(result.getState());
        assertNotNull(result.getCounty());
        assertNotNull(result.getCity());
        assertNotNull(result.getPostalCode());
        assertNotNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        // Test case 7: address with country, state, subAdminArea, locality, postal code, thoroughfare, and coordinate
        addr = new GeoAddress();
        country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        country.getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new Thoroughfare.ThoroughfareNameType());
        addr.setCoordinate(new Coordinate());
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertNotNull(result.getState());
        assertNotNull(result.getCounty());
        assertNotNull(result.getCity());
        assertNotNull(result.getPostalCode());
        assertNotNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertNull(result.getAccuracy());
        
        // Test case 8: address with country, state, subAdminArea, locality, postal code, thoroughfare, coordinate, and accuracy
        addr = new GeoAddress();
        country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        country.setAdministrativeArea(new AdministrativeArea());
        country.getAdministrativeArea().setSubAdministrativeArea(new AdministrativeArea.SubAdministrativeArea());
        country.getAdministrativeArea().getSubAdministrativeArea().getSubAdministrativeAreaName().add(new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        country.getAdministrativeArea().getSubAdministrativeArea().setLocality(new Locality());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getLocalityName().add(new Locality.LocalityName());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setPostalCode(new PostalCode());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getPostalCode().getPostalCodeNumber().add(new PostalCode.PostalCodeNumber());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().setThoroughfare(new Thoroughfare());
        country.getAdministrativeArea().getSubAdministrativeArea().getLocality().getThoroughfare().getThoroughfareName().add(new Thoroughfare.ThoroughfareNameType());
        addr.setCoordinate(new Coordinate());
        addr.setAccuracy(0.9);
        result = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNotNull(result.getCountry());
        assertNotNull(result.getState());
        assertNotNull(result.getCounty());
        assertNotNull(result.getCity());
        assertNotNull(result.getPostalCode());
        assertNotNull(result.getAddressLine1());
        assertNull(result.getAddressLine2());
        assertNotNull(result.getCoordinate());
        assertNotNull(result.getAccuracy());
    }
}